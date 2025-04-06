package com.bingebox.event_service.service;

import com.bingebox.event_service.dto.CreateEventRequest;
import com.bingebox.event_service.model.Event;
import com.bingebox.event_service.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventService {

    @Autowired
    private final EventRepository eventRepository;

    public void createEvent(CreateEventRequest createEventRequest) {
        // Map the CreateEventRequest to Event entity
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .title(createEventRequest.title())
                .description(createEventRequest.description())
                .category(createEventRequest.category())
                .language(createEventRequest.language())
                .ageRating(createEventRequest.ageRating())
                .bannerUrl(createEventRequest.bannerUrl())
                .createdAt(Instant.now())
                .build();
        // Save the event to the database
        eventRepository.save(event);
    }

}
