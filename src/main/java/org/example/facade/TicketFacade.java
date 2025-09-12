package org.example.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.mapper.TicketMapper;
import org.example.repository.GoogleSheetsTicketRepository;
import org.example.service.ManageTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TicketFacade {

    private final TicketMapper mapper;

    private final ManageTicketsService ticketService;

    @Autowired
    private GoogleSheetsTicketRepository sheetsRepo;


    public TicketResponse createTicket(CreateTicketRequest request) {
        log.info("Creating ticket");
        TicketEntity ticket = ticketService.createTicket(request);
        return mapper.toResponse(ticket);
    }

    public TicketResponse updateTicket(String ticketId, UpdateTicketRequest request) {
        log.info("Updating ticket {}", ticketId);
        TicketEntity ticket = ticketService.updateTicket(ticketId, request);
        return mapper.toResponse(ticket);
    }

    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        log.info("Retrieving tickets by status: {}", status);
        List<TicketEntity> ticketEntities = ticketService.getTicketsByStatus(status);

        return ticketEntities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }


}


