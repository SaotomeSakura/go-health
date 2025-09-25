package org.example.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.SheetsServiceProvider;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Repository implementation that persists and retrieves tickets using Google Sheets.
 * Treats a spreadsheet tab as a flat table and maps rows to {@link TicketEntity}.
 * Uses the Sheets API to append and read data, scoped by configuration properties.
 *
 * <p>Configuration:</p>
 * <ul>
 *     <li><code>google.sheets.spreadsheet-id</code></li>
 *     <li><code>google.sheets.tab-name</code></li>
 * </ul>
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class GoogleSheetsTicketRepository implements TicketRepository{

    private final SheetsServiceProvider sheetsProvider;

    @Value("${google.sheets.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${google.sheets.tab-name}")
    private String tabName;

    public TicketEntity saveTicket(TicketEntity ticket) throws TicketRepositoryException {
        try {
            Sheets sheetsService = sheetsProvider.getSheetsService();

            ValueRange body = new ValueRange().setValues(List.of(List.of(
                    ticket.getId().toString(),
                    ticket.getDescription(),
                    ticket.getParentId() != null ? ticket.getParentId().toString() : "",
                    ticket.getStatus().name(),
                    ticket.getCreatedAt().toString(),
                    ticket.getUpdatedAt() != null ? ticket.getUpdatedAt().toString() : ""
            )));

            sheetsService.spreadsheets().values()
                    .append(spreadsheetId, tabName + "!A1", body)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            log.info("Ticket successfully saved to database");

            return ticket;

        } catch (Exception e) {
            throw new TicketRepositoryException("Failed to write ticket to database", e);
        }
    }

    public TicketEntity findById(String ticketId) throws TicketRepositoryException {
        try {
            log.info("Fetching ticket with ID: {}", ticketId);

            Sheets sheetsService = sheetsProvider.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, tabName)
                    .execute();

            List<List<Object>> rows = response.getValues();
            if (rows == null || rows.isEmpty()) {
                return null;
            }

            return rows.stream()
                    .skip(1)
                    .map(this::mapRowToEntity)
                    .filter(ticket -> ticket.getId().equals(ticketId))
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new TicketRepositoryException("Failed to read ticket from database", e);
        }
    }

    public List<TicketEntity> findAllByStatus(TicketStatus status) throws TicketRepositoryException {
        try {
            log.info("Fetching tickets with status: {}", status);

            Sheets sheetsService = sheetsProvider.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, tabName)
                    .execute();

            List<List<Object>> rows = response.getValues();
            if (rows == null || rows.isEmpty()) {
                return List.of();
            }

            return rows.stream()
                    .skip(1)
                    .map(this::mapRowToEntity)
                    .filter(ticket -> ticket.getStatus() == status)
                    .toList();

        } catch (Exception e) {
            throw new TicketRepositoryException("Failed to read tickets from database", e);
        }
    }

    /**
     * Maps a row from the spreadsheet to a {@link TicketEntity}.
     * Assumes column order: ID, Description, Parent ID, Status, Created At, Updated At.
     *
     * @param row the spreadsheet row
     * @return the mapped ticket entity
     */
    private TicketEntity mapRowToEntity(List<Object> row) {
        String id = getCell(row, 0);
        String description = getCell(row, 1);
        String parentId = getCell(row, 2);
        String statusStr = getCell(row, 3);
        String createdAtStr = getCell(row, 4);
        String updatedAtStr = getCell(row, 5);

        return TicketEntity.builder()
                .id(id)
                .description(description)
                .parentId(parentId.isEmpty() ? null : parentId)
                .status(TicketStatus.valueOf(statusStr))
                .createdAt(parseDate(createdAtStr))
                .updatedAt(updatedAtStr.isEmpty() ? null : parseDate(updatedAtStr))
                .build();
    }

    private String getCell(List<Object> row, int index) {
        return index < row.size() ? row.get(index).toString() : "";
    }

    private LocalDateTime parseDate(String dateToParse) {
        try {
            return LocalDateTime.parse(dateToParse);
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format: '{}'. Returning null.", dateToParse);
            return null;
        }
    }
}
