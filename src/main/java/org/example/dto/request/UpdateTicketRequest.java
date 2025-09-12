package org.example.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.TicketStatus;

@Getter
@Setter
public class UpdateTicketRequest {
    @NotNull
    TicketStatus status;

}
