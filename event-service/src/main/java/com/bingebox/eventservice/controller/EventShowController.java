package com.bingebox.eventservice.controller;

import com.bingebox.eventservice.dto.EventShowRequest;
import com.bingebox.eventservice.dto.EventShowResponse;
import com.bingebox.eventservice.service.EventShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events/shows")
@RequiredArgsConstructor
public class EventShowController {

    private final EventShowService eventShowService;

    @PostMapping
    public ResponseEntity<EventShowResponse> createEventShow(@RequestBody EventShowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventShowService.createEventShow(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventShowResponse> getEventShow(@PathVariable String id) {
        return ResponseEntity.ok(eventShowService.getEventShow(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventShowResponse> updateEventShow(@PathVariable String id, @RequestBody EventShowRequest request) {
        return ResponseEntity.ok(eventShowService.updateEventShow(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventShow(@PathVariable String id) {
        eventShowService.deleteEventShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventShowResponse>> getEventShows(
            @RequestParam(name = "event_id", required = false) String eventId,
            @RequestParam(name = "auditorium_id", required = false) String auditoriumId,
            @RequestParam(name = "scheduled_time", required = false) String scheduledTime
    ) {
        List<EventShowResponse> responses = eventShowService.searchEventShows(eventId, auditoriumId, scheduledTime);
        return ResponseEntity.ok(responses);
    }

    // Utility method to parse date string in DD-MM-YYYY format into Instant
    private Instant parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
