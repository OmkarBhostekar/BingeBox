package com.bingebox.eventservice.service;

import com.bingebox.commons.exceptions.EventNotFoundException;
import com.bingebox.commons.exceptions.EventShowNotFoundException;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventShowService {

    private final EventShowRepository eventShowRepository;
    private final EventRepository eventRepository;
    private final AuditoriumServiceGrpc.AuditoriumServiceBlockingStub auditoriumServiceBlockingStub;

    public EventShowResponse createEventShow(EventShowRequest eventShowRequest) {
        validateSeatPricing(eventShowRequest);

        Event event = eventRepository.findById(UUID.fromString(eventShowRequest.eventId()))
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        AuditoriumResponse auditorium = fetchAuditorium(eventShowRequest.auditoriumId());

        double basePrice = eventShowRequest.seatPricing().stream()
                .mapToDouble(EventShowRequest.SeatPricing::price)
                .min()
                .orElseThrow(() -> new IllegalArgumentException("Seat pricing is empty"));

        EventShow eventShow = EventShow.builder()
                .event(event)
                .auditoriumId(eventShowRequest.auditoriumId())
                .scheduledTime(eventShowRequest.scheduledTime().toInstant(ZoneOffset.UTC))
                .basePrice(basePrice)
                .build();

        return EventShowMapper.toEventShowResponse(auditorium, eventShowRepository.save(eventShow));
    }

    public EventShowResponse getEventShow(String id) {
        EventShow eventShow = findEventShowById(id);
        return EventShowMapper.toEventShowResponse(fetchAuditorium(eventShow.getAuditoriumId()), eventShow);
    }

    public EventShowResponse updateEventShow(String id, EventShowRequest eventShowRequest) {
        EventShow eventShow = findEventShowById(id);

        if (eventShowRequest.scheduledTime() != null) {
            eventShow.setScheduledTime(eventShowRequest.scheduledTime().toInstant(ZoneOffset.UTC));
        }

        if (eventShowRequest.seatPricing() != null && !eventShowRequest.seatPricing().isEmpty()) {
            double basePrice = eventShowRequest.seatPricing().stream()
                    .mapToDouble(EventShowRequest.SeatPricing::price)
                    .min()
                    .orElseThrow(() -> new IllegalArgumentException("Seat pricing is empty"));
            eventShow.setBasePrice(basePrice);
        }

        return EventShowMapper.toEventShowResponse(fetchAuditorium(eventShow.getAuditoriumId()), eventShowRepository.save(eventShow));
    }

    public void deleteEventShow(String id) {
        eventShowRepository.delete(findEventShowById(id));
    }

    public List<EventShowResponse> searchEventShows(String eventId, String auditoriumId, String scheduledTime) {
        Instant[] timeRange = parseScheduledTime(scheduledTime);
        List<EventShow> eventShows = fetchEventShows(eventId, auditoriumId, timeRange[0], timeRange[1]);
        return mapEventShowsToResponses(eventShows);
    }

    private Instant[] parseScheduledTime(String scheduledTime) {
        Instant startOfDay = null, endOfDay = null;

        if (scheduledTime != null && !scheduledTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                LocalDate localDate = LocalDate.parse(scheduledTime, formatter);
                startOfDay = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                endOfDay = localDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusMillis(1);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid scheduled time format, expected DD-MM-YYYY");
            }
        }

        return new Instant[]{startOfDay, endOfDay};
    }

    private List<EventShow> fetchEventShows(String eventId, String auditoriumId, Instant startOfDay, Instant endOfDay) {
        if (eventId != null && !eventId.isEmpty()) {
            UUID eventUUID = UUID.fromString(eventId);
            if (auditoriumId != null && !auditoriumId.isEmpty()) {
                return eventShowRepository.findByEventIdAndAuditoriumIdAndScheduledTimeBetween(eventUUID, auditoriumId, startOfDay, endOfDay);
            } else {
                return eventShowRepository.findByEventIdAndScheduledTimeBetween(eventUUID, startOfDay, endOfDay);
            }
        } else if (auditoriumId != null && !auditoriumId.isEmpty()) {
            return eventShowRepository.findByAuditoriumIdAndScheduledTimeBetween(auditoriumId, startOfDay, endOfDay);
        } else {
            return eventShowRepository.findByScheduledTimeBetween(startOfDay, endOfDay);
        }
    }

    private List<EventShowResponse> mapEventShowsToResponses(List<EventShow> eventShows) {
        if (eventShows.isEmpty()) {
            throw new EventShowNotFoundException("No event shows found");
        }

        return eventShows.stream()
                .map(eventShow -> EventShowMapper.toEventShowResponse(fetchAuditorium(eventShow.getAuditoriumId()), eventShow))
                .collect(Collectors.toList());
    }

    private EventShow findEventShowById(String id) {
        return eventShowRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EventShowNotFoundException("Event show not found"));
    }

    private void validateSeatPricing(EventShowRequest eventShowRequest) {
        if (eventShowRequest.seatPricing() == null || eventShowRequest.seatPricing().isEmpty()) {
            throw new IllegalArgumentException("Seat pricing must be provided");
        }
    }

    private AuditoriumResponse fetchAuditorium(String auditoriumId) {
        try {
            return auditoriumServiceBlockingStub.getAuditorium(
                    GetAuditoriumRequest.newBuilder().setAuditoriumId(auditoriumId).build()
            );
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Failed to fetch auditorium: " + e.getStatus().getDescription());
        }
    }
}
