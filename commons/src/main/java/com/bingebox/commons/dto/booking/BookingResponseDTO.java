package com.bingebox.commons.dto.booking;

import com.bingebox.commons.dto.event.EventShowResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BookingResponseDTO(
        @JsonProperty("event_details") EventShowResponse eventShowResponse,
        @JsonProperty("booked_seats") List<BookingSeatDTO> bookingSeats,
        @JsonProperty("user_id") String userId
) { }

