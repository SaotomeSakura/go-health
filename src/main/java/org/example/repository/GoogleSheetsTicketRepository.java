package org.example.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.example.client.GoogleSheetsClient;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class GoogleSheetsTicketRepository implements TicketRepository{

    @Value("${google.sheets.spreadsheet-id}")
    private String spreadsheetId = "1CiWwtCAEYbv2qNN6t3GzdJqGoMSy5ktgP7SIQ7DmY4c";

    public TicketEntity saveTicket(TicketEntity ticket) {
        try {
            Sheets sheetsService = GoogleSheetsClient.getSheetsService();

            ValueRange body = new ValueRange().setValues(List.of(List.of(
                    ticket.getId().toString(),
                    ticket.getDescription(),
                    ticket.getParentId() != null ? ticket.getParentId().toString() : "",
                    ticket.getStatus().name(),
                    ticket.getCreatedAt().toString(),
                    ticket.getUpdatedAt() != null ? ticket.getUpdatedAt().toString() : ""
            )));

            sheetsService.spreadsheets().values()
                    .append(spreadsheetId, "List 1!A1", body)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            log.info("Ticket successfully saved to database");

            return ticket;

        } catch (Exception e) {
            throw new RuntimeException("Failed to write ticket to database", e);
        }
    }

    public TicketEntity findById(String ticketId) {
        try {
            Sheets sheetsService = GoogleSheetsClient.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, "List 1")
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
            throw new RuntimeException("Failed to read ticket from database", e);
        }
    }

    public List<TicketEntity> findAllByStatus(TicketStatus status) {
        try {
            Sheets sheetsService = GoogleSheetsClient.getSheetsService();

            ValueRange response = sheetsService.spreadsheets().values()
                    .get(spreadsheetId, "List 1") // Use your actual tab name
                    .execute();

            List<List<Object>> rows = response.getValues();
            if (rows == null || rows.isEmpty()) {
                return List.of();
            }

            return rows.stream()
                    .skip(1) // Skip header row
                    .map(this::mapRowToEntity)
                    .filter(ticket -> ticket.getStatus() == status)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to read tickets from database", e);
        }
    }

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
                .createdAt(LocalDateTime.parse(createdAtStr))
                .updatedAt(updatedAtStr.isEmpty() ? null : LocalDateTime.parse(updatedAtStr))
                .build();

    }

    private String getCell(List<Object> row, int index) {
        return index < row.size() ? row.get(index).toString() : "";
    }
}
