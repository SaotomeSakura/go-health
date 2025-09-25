package org.example.facade;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.mapper.TicketMapper;
import org.example.mapper.TicketMapperImpl;
import org.example.service.ManageTicketsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.example.enums.TicketStatus.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketFacadeTest {

    @Mock
    private ManageTicketsService ticketService;

    private TicketMapper mapper = Mappers.getMapper(TicketMapper.class);

    @InjectMocks
    private TicketFacade facade;

    @BeforeEach
    void setUp() {
        mapper = new TicketMapperImpl();
        facade = new TicketFacade(mapper, ticketService);
    }


    @Test
    void createTicket_shouldReturnMappedResponse() throws TicketRepositoryException {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setDescription("Test");

        TicketEntity entity = TicketEntity.builder().id("AD-1").description("Test").build();

        when(ticketService.createTicket(request)).thenReturn(entity);

        TicketResponse result = facade.createTicket(request);

        assertEquals("AD-1", result.getId());
        assertEquals("Test", result.getDescription());
    }

    @Test
    void updateTicket_shouldReturnUpdatedResponse() throws TicketRepositoryException {
        String ticketId = "AD-1";
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setStatus(TicketStatus.IN_PROGRESS);

        TicketEntity entity = TicketEntity.builder().id(ticketId).status(TicketStatus.IN_PROGRESS).build();

        when(ticketService.updateTicket(ticketId, request)).thenReturn(entity);

        TicketResponse result = facade.updateTicket(ticketId, request);

        assertEquals("IN_PROGRESS", result.getStatus());
    }

    @Test
    void updateTicket_ticketNotFound() throws TicketRepositoryException {
        String ticketId = "AD-404";
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setStatus(TicketStatus.CLOSED);

        when(ticketService.updateTicket(ticketId, request))
                .thenThrow(new EntityNotFoundException("Ticket with ID AD-404 not found"));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                facade.updateTicket(ticketId, request)
        );

        assertEquals("Ticket with ID AD-404 not found", ex.getMessage());
    }


    @Test
    void getTicketsByStatus_shouldMapEntitiesToResponses() throws TicketRepositoryException {
        List<TicketEntity> entities = List.of(
                TicketEntity.builder().id("AD-1").status(OPEN).build()
        );

        when(ticketService.getTicketsByStatus(OPEN)).thenReturn(entities);

        List<TicketResponse> result = facade.getTicketsByStatus(OPEN);

        assertEquals(1, result.size());
        assertEquals("AD-1", result.get(0).getId());
    }

}