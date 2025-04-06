package com.bingebox.event_service.controller;

import com.bingebox.event_service.dto.CreateEventRequest;
import com.bingebox.event_service.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
public class EventController {

    @Autowired
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createEvent(@RequestBody CreateEventRequest request) {
        eventService.createEvent(request);
        return "Event created successfully!";
    }

}
