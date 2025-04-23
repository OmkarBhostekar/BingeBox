package com.bingebox.eventservice.model;

import jakarta.persistence.*;
import lombok.*;

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
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "auditorium_id", nullable = false)
    private String auditoriumId; // Auditorium ID is from VenueService

    private Instant scheduledTime;
    private Double basePrice;
}
