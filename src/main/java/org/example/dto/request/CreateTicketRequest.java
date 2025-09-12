package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {
    @NotNull(message = "Description must not be null")
    private String description;

    private String parentId;

}
