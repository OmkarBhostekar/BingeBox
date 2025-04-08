package com.bingebox.eventservice.dto;

import com.bingebox.commons.enums.SeatType;

import java.time.LocalDateTime;
import java.util.List;

public record EventShowRequest(
        String eventId,
        String auditoriumId,
        LocalDateTime scheduledTime,
        List<SeatPricing> seatPricing
) {
    public record SeatPricing(SeatType seatType, Double price) {}
}