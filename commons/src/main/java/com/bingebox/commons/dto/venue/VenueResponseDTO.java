package com.bingebox.commons.dto.venue;

public record VenueResponseDTO(
        String id,
        String name,
        String address,
        String pin_code,
        String contact_number,
        float latitude,
        float longitude
) {}
