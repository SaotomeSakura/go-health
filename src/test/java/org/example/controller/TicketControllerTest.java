package org.example.controller;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.facade.TicketFacade;
import org.example.mapper.TicketMapper;
import org.example.mapper.TicketMapperImpl;
import org.example.service.ManageTicketsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private ManageTicketsService ticketService;

    private TicketMapper mapper = new TicketMapperImpl();
    private TicketFacade ticketFacade;
    private TicketController controller;

    @BeforeEach
    void setUp() {
        ticketFacade = new TicketFacade(mapper, ticketService);
        controller = new TicketController(ticketFacade);
    }


    @Test
    void createTicket_shouldReturnResponse() throws TicketRepositoryException {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setDescription("Test");

        TicketEntity entity = TicketEntity.builder().id("AD-1").description("Test").build();
        when(ticketService.createTicket(request)).thenReturn(entity);

        ResponseEntity<TicketResponse> result = controller.createTicket(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("AD-1", result.getBody().getId());
        assertEquals("Test", result.getBody().getDescription());
    }

    @Test
    void updateTicket_shouldReturnUpdatedResponse() throws TicketRepositoryException {
        String ticketId = "AD-1";
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setStatus(TicketStatus.IN_PROGRESS);

        TicketResponse response = TicketResponse.builder()
                .id(ticketId)
                .status("IN_PROGRESS")
                .build();

        when(ticketService.updateTicket(ticketId, request)).thenReturn(
                TicketEntity.builder().id(ticketId).status(TicketStatus.IN_PROGRESS).build()
        );

        ResponseEntity<TicketResponse> result = controller.updateTicket(ticketId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("IN_PROGRESS", result.getBody().getStatus());
        assertEquals(ticketId, result.getBody().getId());
    }

    @Test
    void getTicketsByStatus_shouldReturnList() throws TicketRepositoryException {
        TicketEntity entity = TicketEntity.builder().id("AD-1").status(TicketStatus.OPEN).build();
        when(ticketService.getTicketsByStatus(TicketStatus.OPEN)).thenReturn(List.of(entity));

        ResponseEntity<List<TicketResponse>> result = controller.getTicketsByStatus(TicketStatus.OPEN);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("AD-1", result.getBody().get(0).getId());
        assertEquals("OPEN", result.getBody().get(0).getStatus());
    }


}
