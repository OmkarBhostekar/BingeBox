package com.bingebox.commons.dto;

public record AuditoriumResponseDTO(
        String id,
        String name,
        int capacity,
        VenueResponseDTO venue
) {}