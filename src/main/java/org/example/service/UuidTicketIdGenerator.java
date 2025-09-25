package org.example.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidTicketIdGenerator implements TicketIdGenerator {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}

