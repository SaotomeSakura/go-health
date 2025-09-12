package org.example.service;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;

import java.util.List;

public interface ManageTicketsService {

    TicketEntity createTicket(CreateTicketRequest request);
    TicketEntity updateTicket(String ticketId, UpdateTicketRequest request);
    List<TicketEntity> getTicketsByStatus(TicketStatus status);


}
