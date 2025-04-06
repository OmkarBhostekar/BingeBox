package com.bingebox.event_service.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String title;
    private String description;
    private String category;
    private String language;
    private String ageRating;
    private String bannerUrl;

    private Instant createdAt;
}
