package com.bingebox.eventservice.service;

import com.bingebox.commons.venue.service.AuditoriumResponse;
import com.bingebox.commons.venue.service.AuditoriumServiceGrpc;
import com.bingebox.commons.venue.service.GetAuditoriumRequest;
import com.bingebox.eventservice.dto.EventShowRequest;
import com.bingebox.eventservice.dto.EventShowResponse;
import com.bingebox.eventservice.mappers.EventShowMapper;
import com.bingebox.eventservice.model.Event;
import com.bingebox.eventservice.model.EventShow;
import com.bingebox.eventservice.repository.EventRepository;
import com.bingebox.eventservice.repository.EventShowRepository;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventShowService {

    private final EventShowRepository eventShowRepository;

    private final EventRepository eventRepository;

    private final AuditoriumServiceGrpc.AuditoriumServiceBlockingStub auditoriumServiceBlockingStub;

    public EventShowResponse createEventShow(EventShowRequest eventShowRequest) {
        if (eventShowRequest.seatPricing() == null || eventShowRequest.seatPricing().isEmpty()) {
            throw new IllegalArgumentException("Seat pricing must be provided");
        }

        // Get Auditorium from gRPC
        AuditoriumResponse auditorium;
        try {
            auditorium = auditoriumServiceBlockingStub.getAuditorium(
                    GetAuditoriumRequest.newBuilder().setAuditoriumId(eventShowRequest.auditoriumId()).build());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Failed to fetch auditorium: " + e.getStatus().getDescription());
        }

        // Get Event
        Event event = eventRepository.findById(UUID.fromString(eventShowRequest.eventId()))
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Calculate base price
        double basePrice = eventShowRequest.seatPricing().stream()
                .mapToDouble(EventShowRequest.SeatPricing::price)
                .min()
                .orElseThrow(() -> new IllegalArgumentException("Seat pricing is empty"));

        // Create EventShow
        EventShow eventShow = EventShow.builder()
                .event(event)
                .auditoriumId(eventShowRequest.auditoriumId())
                .scheduledTime(eventShowRequest.scheduledTime().toInstant(ZoneOffset.UTC))
                .basePrice(basePrice)
                .build();

        eventShow = eventShowRepository.save(eventShow);

        // Map to response
        return EventShowMapper.toEventShowResponse(auditorium, eventShow);
    }


}
