package com.bingebox.event_service.mappers;

import com.bingebox.event_service.dto.EventRequest;
import com.bingebox.event_service.dto.EventResponse;
import com.bingebox.event_service.model.Event;

import java.time.Instant;

public class EventMapper {

    public static EventResponse mapToResponse(Event event) {
        return new EventResponse(
            event.getId().toString(),
            event.getTitle(),
            event.getDescription(),
            event.getCategory(),
            event.getLanguage(),
            event.getAgeRating(),
            event.getBannerUrl(),
            event.getCreatedAt().toString()
        );
    }

    public static Event mapToEntity(EventRequest eventRequest) {
        return Event.builder()
            .title(eventRequest.title())
            .description(eventRequest.description())
            .category(eventRequest.category())
            .language(eventRequest.language())
            .ageRating(eventRequest.ageRating())
            .bannerUrl(eventRequest.bannerUrl())
            .createdAt(Instant.now())
            .build();
    }
}
