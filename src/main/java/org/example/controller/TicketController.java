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
import org.example.facade.TicketFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketFacade ticketFacade;


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
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest requestBody) {

        TicketResponse response = ticketFacade.createTicket(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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
                                                       @Valid @RequestBody UpdateTicketRequest requestBody) {

        TicketResponse response = ticketFacade.updateTicket(ticketId, requestBody);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

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
    public ResponseEntity<List<TicketResponse>> getTicketsByStatus(@PathVariable TicketStatus status) {

        List<TicketResponse> response = ticketFacade.getTicketsByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
