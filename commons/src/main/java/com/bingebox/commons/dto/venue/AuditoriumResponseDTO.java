package com.bingebox.commons.dto.venue;

public record AuditoriumResponseDTO(
        String id,
        String name,
        int capacity,
        VenueResponseDTO venue
) {}