package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse {
    @NotNull
    String id;

    @NotNull
    String description;

    String parentId;

    @NotNull
    String status;

    @NotNull
    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
