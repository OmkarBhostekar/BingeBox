package com.bingebox.event_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event_shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShow {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "auditorium_id", nullable = false)
    private UUID auditoriumId; // Auditorium ID is from VenueService

    private Instant scheduledTime;
    private Integer basePrice;
}
