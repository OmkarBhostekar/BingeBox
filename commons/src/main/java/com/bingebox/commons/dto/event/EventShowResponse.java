package com.bingebox.commons.dto.event;

import com.bingebox.commons.dto.venue.AuditoriumResponseDTO;


public record EventShowResponse(
        String id,
        EventResponse event,
        AuditoriumResponseDTO auditorium,
        String scheduledTime
) {}