package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.mapper.TicketMapper;
import org.example.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ManageTicketsServiceImpl implements ManageTicketsService  {

    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;

    public ManageTicketsServiceImpl(TicketRepository ticketRepository, TicketMapper mapper) {
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }

    public TicketEntity createTicket(CreateTicketRequest request) {
        log.info("Creating new ticket with description: {}", request.getDescription());

        TicketEntity entity = mapper.toEntity(request);
        entity.setStatus(TicketStatus.OPEN);
        entity.setId("AD-" + UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());

        return ticketRepository.saveTicket(entity);
    }

    public TicketEntity updateTicket(String ticketId, UpdateTicketRequest request) {
        log.info("Updating ticket {} to status {}", ticketId, request.getStatus());

        TicketEntity entity = getTicketEntity(ticketId);
        entity.setStatus(request.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.saveTicket(entity);
    }

    public List<TicketEntity> getTicketsByStatus(TicketStatus status) {
        log.info("Fetching tickets with status: {}", status);
        return ticketRepository.findAllByStatus(status);
    }

    private TicketEntity getTicketEntity(String ticketId) {
        TicketEntity entity = ticketRepository.findById(ticketId);
        if (entity == null ) {
            throw new EntityNotFoundException("Ticket with ID " + ticketId + " not found");
        }
        return entity;
    }
}
