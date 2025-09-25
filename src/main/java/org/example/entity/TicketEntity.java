package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Entity
@Table(name = "tickets")
public class TicketEntity {
    @Id
    @GeneratedValue
    private String id;

    private String description;
    private String parentId;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //posibility of migrating to JPA/Hibernate.
    @PrePersist
    public void onCreate() {
        log.info("Setting createdAt for ticket {}", id);
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        log.info("Updating updatedAt for ticket {}", id);
        this.updatedAt = LocalDateTime.now();
    }

}
