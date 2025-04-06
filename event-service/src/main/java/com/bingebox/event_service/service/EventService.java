package com.bingebox.event_service.service;

import com.bingebox.event_service.dto.EventRequest;
import com.bingebox.event_service.dto.EventResponse;
import com.bingebox.event_service.mappers.EventMapper;
import com.bingebox.event_service.model.Event;
import com.bingebox.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse createEvent(EventRequest request) {
        Event event = EventMapper.mapToEntity(request);
        event.setId(UUID.randomUUID());
        eventRepository.save(event);
        return EventMapper.mapToResponse(event);
    }

    public EventResponse getEvent(UUID id) {
        return eventRepository.findById(id)
                .map(EventMapper::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::mapToResponse)
                .toList();
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }

    public EventResponse updateEvent(UUID id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Patch only non-null fields
        if (request.title() != null) event.setTitle(request.title());
        if (request.description() != null) event.setDescription(request.description());
        if (request.category() != null) event.setCategory(request.category());
        if (request.language() != null) event.setLanguage(request.language());
        if (request.ageRating() != null) event.setAgeRating(request.ageRating());
        if (request.bannerUrl() != null) event.setBannerUrl(request.bannerUrl());

        eventRepository.save(event);
        return EventMapper.mapToResponse(event);
    }
}
