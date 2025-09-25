package org.example.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.mapper.TicketMapper;
import org.example.repository.GoogleSheetsTicketRepository;
import org.example.service.ManageTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade layer for ticket operations.
 * Converts domain entities to response DTOs using {@link TicketMapper}.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TicketFacade {

    private final TicketMapper mapper;

    private final ManageTicketsService ticketService;

    private GoogleSheetsTicketRepository googleSheetsTicketRepository;


    /**
     * Creates a new ticket based on the provided request.
     * Delegates creation to the {@link ManageTicketsService} and maps the result to a response DTO.
     *
     * @param request the ticket creation request
     * @return the created ticket as a {@link TicketResponse}
     */
    public TicketResponse createTicket(CreateTicketRequest request) throws TicketRepositoryException {
        log.info("Creating ticket");
        TicketEntity ticket = ticketService.createTicket(request);
        return mapper.toResponse(ticket);
    }

    /**
     * Updates the status of an existing ticket.
     * Delegates update logic to the {@link ManageTicketsService} and maps the result to a response DTO.
     *
     * @param ticketId the ID of the ticket to update
     * @param request the update request containing the new status
     * @return the updated ticket as a {@link TicketResponse}
     */
    public TicketResponse updateTicket(String ticketId, UpdateTicketRequest request) throws TicketRepositoryException {
        log.info("Updating ticket {}", ticketId);
        TicketEntity ticket = ticketService.updateTicket(ticketId, request);
        return mapper.toResponse(ticket);
    }

    /**
     * Retrieves all tickets matching the specified status.
     * Delegates filtering to the {@link ManageTicketsService} and maps results to response DTOs.
     *
     * @param status the status to filter tickets by
     * @return a list of matching tickets as {@link TicketResponse} objects
     */
    public List<TicketResponse> getTicketsByStatus(TicketStatus status) throws TicketRepositoryException {
        log.info("Retrieving tickets by status: {}", status);
        List<TicketEntity> ticketEntities = ticketService.getTicketsByStatus(status);

        return ticketEntities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }


}


