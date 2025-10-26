package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.mapper.TicketMapper;
import org.example.mapper.TicketMapperImpl;
import org.example.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.enums.TicketStatus.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManageTicketsServiceImplTest {

    @InjectMocks
    ManageTicketsServiceImpl service;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketIdGenerator idGenerator;

    private TicketMapper ticketMapper = Mappers.getMapper(TicketMapper.class);


    @BeforeEach
    void setUp() {
        ticketMapper = new TicketMapperImpl();
        service = new ManageTicketsServiceImpl(ticketRepository, ticketMapper, idGenerator);
    }



    @Test
    public void createTicket_OK() throws TicketRepositoryException {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setDescription("Test description");
        request.setParentId("AD-01");

        TicketEntity responseEntity =
                new TicketEntity().builder()
                        .description("Test description")
                        .parentId("AD-01")
                        .status(OPEN)
                        .createdAt(LocalDateTime.now())
                        .build();

        when(ticketRepository.saveTicket(any())).thenReturn(responseEntity);


        TicketEntity entity = service.createTicket(request);

        assertNotNull(entity);
        assertEquals(request.getDescription(), entity.getDescription());
        assertNotNull(entity.getStatus());
        assertNotNull(entity.getCreatedAt());

//        verify(ticketRepository).save(any());
    }

    @Test
    public void updateTicket_OK() throws TicketRepositoryException {
        String ticketId = "AD-1";
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setStatus(TicketStatus.CLOSED);

        TicketEntity existing = TicketEntity.builder().id(ticketId).status(TicketStatus.OPEN).build();
        TicketEntity updated = TicketEntity.builder().id(ticketId).status(TicketStatus.CLOSED).build();

        when(ticketRepository.findById(ticketId)).thenReturn(existing);
        when(ticketRepository.saveTicket(existing)).thenReturn(updated);

        TicketEntity result = service.updateTicket(ticketId, request);

        assertEquals(TicketStatus.CLOSED, result.getStatus());
        verify(ticketRepository).saveTicket(existing);

    }

    @Test
    void updateTicket_ticketNotFound() throws TicketRepositoryException {
        String ticketId = "AD-404";
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findById(ticketId)).thenReturn(null);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                service.updateTicket(ticketId, request)
        );

        assertEquals("Ticket with ID AD-404 not found", ex.getMessage());
    }


    @Test
    public void getTicketsByStatus() throws TicketRepositoryException {
        List<TicketEntity> tickets = List.of(
                TicketEntity.builder().id("AD-1").status(TicketStatus.OPEN).build()
        );

        when(ticketRepository.findAll()).thenReturn(tickets);

        List<TicketEntity> result = service.getTicketsByStatus(OPEN);

        assertEquals(1, result.size());
        assertEquals("AD-1", result.get(0).getId());

    }

    @Test
    void getTicketsByStatus_shouldThrowException_whenStatusInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getTicketsByStatus(TicketStatus.valueOf("INVALID"))
        );
    }

}