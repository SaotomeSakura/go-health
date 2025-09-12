package org.example.mapper;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketEntity toEntity(CreateTicketRequest request);

    TicketResponse toResponse(TicketEntity entity);

}
