package com.bingebox.eventservice.dto;

import com.bingebox.commons.dto.AuditoriumResponseDTO;
import com.bingebox.commons.venue.service.AuditoriumResponse;


public record EventShowResponse(
        String id,
        EventResponse event,
        AuditoriumResponseDTO auditorium,
        String scheduledTime
) {}