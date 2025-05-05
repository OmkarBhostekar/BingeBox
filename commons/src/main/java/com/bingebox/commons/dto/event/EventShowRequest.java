package com.bingebox.commons.dto.event;

import com.bingebox.commons.enums.SeatType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record EventShowRequest(
        @JsonProperty("event_id") String eventId,
        @JsonProperty("auditorium_id") String auditoriumId,
        @JsonProperty("scheduled_time") LocalDateTime scheduledTime,
        @JsonProperty("seat_pricing") List<SeatPricing> seatPricing
) {
    public record SeatPricing(
            @JsonProperty("seat_type") SeatType seatType,
            @JsonProperty("price") Double price
    ) {}
}