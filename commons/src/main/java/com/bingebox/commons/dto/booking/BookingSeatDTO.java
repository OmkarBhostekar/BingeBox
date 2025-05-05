package com.bingebox.commons.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingSeatDTO(
        @JsonProperty("seat_id") String seatId,
        @JsonProperty("seat_type") String seatType,
        @JsonProperty("seat_number") String seatNumber,
        @JsonProperty("seat_status") String status
) { }
