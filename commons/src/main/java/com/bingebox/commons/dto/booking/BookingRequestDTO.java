package com.bingebox.commons.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BookingRequestDTO(
        @JsonProperty("event_show_id") String eventShowId,
        @JsonProperty("booking_seats") List<BookingSeatDTO> bookingSeats,
        @JsonProperty("user_id") String userId
) { }

