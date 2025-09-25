package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request payload for creating a new ticket.
 * Contains the required description and an optional parent ticket ID.
 */
@Getter
@Setter
public class CreateTicketRequest {
    @NotNull(message = "Description must not be null")
    private String description;

    private String parentId;

}
