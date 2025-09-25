package org.example.repository;

import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;

import java.util.List;

public interface TicketRepository {
    TicketEntity saveTicket(TicketEntity ticket) throws TicketRepositoryException;
    TicketEntity findById(String ticketId) throws TicketRepositoryException;
    List<TicketEntity> findAllByStatus(TicketStatus status) throws TicketRepositoryException;
}

