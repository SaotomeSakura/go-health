package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateTicketRequest;
import org.example.dto.request.UpdateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.enums.TicketStatus;
import org.example.exception.TicketRepositoryException;
import org.example.facade.TicketFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing tickets.
 * Exposes endpoints for creating, updating, and retrieving tickets via HTTP.
 * Delegates business logic to the {@link TicketFacade}.
 *
 * <p>Base path: <code>/api/tickets</code></p>
 */
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketFacade ticketFacade;


    /**
     * Creates a new ticket.
     *
     * @param requestBody the ticket creation request
     * @return the created ticket wrapped in a {@link ResponseEntity}
     * @throws TicketRepositoryException if persistence fails
     */
    @PostMapping("/create")
    @Tag(name = "Tickets")
    @Operation(summary = "Creates ticket of the issue in the database", description = "Creates ticket of the issue in the database", responses = {
            @ApiResponse(responseCode = "201",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Field validation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = APPLICATION_JSON_VALUE)
            )
    })
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest requestBody) throws TicketRepositoryException {

        TicketResponse response = ticketFacade.createTicket(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates the status of an existing ticket.
     *
     * @param ticketId the ID of the ticket to update
     * @param requestBody the update request containing the new status
     * @return the updated ticket wrapped in a {@link ResponseEntity}
     * @throws TicketRepositoryException if persistence fails
     */
    @PutMapping("/{ticketId}")
    @Tag(name = "Tickets")
    @Operation(summary = "Updates ticket in the database", description = "Updates ticket in the database", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Field validation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = APPLICATION_JSON_VALUE)
            )
    })
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable String ticketId,
                                                       @Valid @RequestBody UpdateTicketRequest requestBody) throws TicketRepositoryException {

        TicketResponse response = ticketFacade.updateTicket(ticketId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves all tickets matching the specified status.
     *
     * @param status the status to filter tickets by
     * @return a list of matching tickets wrapped in a {@link ResponseEntity}
     * @throws TicketRepositoryException if retrieval fails
     */
    @GetMapping("/find/status/{status}")
    @Tag(name = "Tickets")
    @Operation(summary = "Returns all the tickets by their status", description = "Returns all the tickets by their status", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Field validation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            mediaType = APPLICATION_JSON_VALUE)
            )
    })
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@PathVariable TicketStatus status) throws TicketRepositoryException {

        List<TicketResponse> response = ticketFacade.getTicketsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
