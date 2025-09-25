package org.example.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.TicketStatus;

/**
 * Request payload for updating the status of an existing ticket.
 */
@Getter
@Setter
public class UpdateTicketRequest {
    @NotNull
    TicketStatus status;

}
