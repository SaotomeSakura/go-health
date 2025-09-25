package org.example.service;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;

import java.util.List;

/**
 * Service interface for managing ticket operations.
 * Defines core business actions such as creating, updating, and retrieving tickets.
 */
public interface ManageTicketsService {

    TicketEntity createTicket(CreateTicketRequest request) throws TicketRepositoryException;
    TicketEntity updateTicket(String ticketId, UpdateTicketRequest request) throws TicketRepositoryException;
    List<TicketEntity> getTicketsByStatus(TicketStatus status) throws TicketRepositoryException;

}
