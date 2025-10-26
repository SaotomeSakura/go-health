package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.mapper.TicketMapper;
import org.example.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Locale.filter;

/**
 * Implementation of {@link ManageTicketsService} that handles core ticket operations.
 * Responsible for creating, updating, and retrieving tickets, and delegating persistence to the {@link TicketRepository}.
 * Applies business rules such as default status assignment and timestamping.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ManageTicketsServiceImpl implements ManageTicketsService  {

    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;
    private final TicketIdGenerator idGenerator;

    /**
     * Creates a new ticket with default status and timestamp.
     * Generates a unique ID and delegates persistence to the repository.
     *
     * @param request the ticket creation request
     * @return the persisted {@link TicketEntity}
     * @throws TicketRepositoryException if saving fails
     */
    public TicketEntity createTicket(CreateTicketRequest request) throws TicketRepositoryException {
        log.info("Creating new ticket with description: {}", request.getDescription());

        TicketEntity entity = mapper.toEntity(request);
        entity.setStatus(TicketStatus.OPEN);
        entity.setId(idGenerator.generateId());
        entity.setCreatedAt(LocalDateTime.now());

        return ticketRepository.saveTicket(entity);
    }

    /**
     * Updates the status of an existing ticket and sets the updated timestamp.
     * Validates existence before applying changes.
     *
     * @param ticketId the ID of the ticket to update
     * @param request the update request containing the new status
     * @return the updated {@link TicketEntity}
     * @throws TicketRepositoryException if saving fails
     * @throws EntityNotFoundException if the ticket does not exist
     */
    public TicketEntity updateTicket(String ticketId, UpdateTicketRequest request) throws TicketRepositoryException {
        log.info("Updating ticket {} to status {}", ticketId, request.getStatus());

        TicketEntity entity = getTicketEntity(ticketId);
        entity.setStatus(request.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.saveTicket(entity);
    }

    /**
     * Retrieves all tickets matching the specified status.
     *
     * @param status the status to filter by
     * @return a list of matching {@link TicketEntity} objects
     * @throws TicketRepositoryException if retrieval fails
     */
    public List<TicketEntity> getTicketsByStatus(TicketStatus status) throws TicketRepositoryException {
        log.info("Fetching tickets with status: {}", status);
        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getStatus() == status)
                .toList();
    }

    /**
     * Helper method to retrieve a ticket by ID.
     *
     * @param ticketId the ID of the ticket
     * @return the matching {@link TicketEntity}
     * @throws EntityNotFoundException if no ticket is found
     */
    private TicketEntity getTicketEntity(String ticketId) throws TicketRepositoryException {
        TicketEntity entity = ticketRepository.findById(ticketId);
        if (entity == null ) {
            throw new EntityNotFoundException("Ticket with ID " + ticketId + " not found");
        }
        return entity;
    }
}
