package com.bingebox.eventservice.dto;

import com.bingebox.commons.venue.service.AuditoriumResponse;


public record EventShowResponse(
        String id,
        EventResponse event,
        AuditoriumResponse auditorium,
        String scheduledTime,
        Double basePrice
) {}