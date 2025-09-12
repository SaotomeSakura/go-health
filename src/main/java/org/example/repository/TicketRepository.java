package org.example.repository;

import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;

import java.util.List;

public interface TicketRepository {
    TicketEntity saveTicket(TicketEntity ticket);
    TicketEntity findById(String ticketId);
    List<TicketEntity> findAllByStatus(TicketStatus status);
}

