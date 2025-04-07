package com.bingebox.event_service.dto;

import com.bingebox.commons.enums.SeatType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventShowRequest(
        UUID eventId,
        UUID auditoriumId,
        LocalDateTime scheduledTime,
        List<SeatPricing> seatPricing
) {
    public record SeatPricing(SeatType seatType, Double price) {}
}