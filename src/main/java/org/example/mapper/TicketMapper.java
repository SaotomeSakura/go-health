package org.example.mapper;

import org.example.dto.request.CreateTicketRequest;
import org.example.dto.response.TicketResponse;
import org.example.entity.TicketEntity;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between ticket domain entities and transport-layer DTOs.
 * Automatically implemented by MapStruct at build time.
 *
 * <p>Mappings:</p>
 * <ul>
 *     <li>{@link CreateTicketRequest} → {@link TicketEntity}</li>
 *     <li>{@link TicketEntity} → {@link TicketResponse}</li>
 * </ul>
 *
 * <p>Registered as a Spring component via {@code @Mapper(componentModel = "spring")}.</p>
 */
@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketEntity toEntity(CreateTicketRequest request);

    TicketResponse toResponse(TicketEntity entity);

}
