package com.bingebox.venueservice.mappers;

import com.bingebox.commons.enums.SeatType;
import com.bingebox.commons.venue.service.*;
import com.bingebox.venueservice.model.Seat;

import java.util.List;
import java.util.stream.Collectors;

public class SeatMapper {

    // Maps CreateSeatRequest to Seat entity
    public static Seat toEntity(com.bingebox.commons.venue.service.Seat request) {
        if (request == null) {
            return null; // Handle null input gracefully
        }
        return Seat.builder()
                .seatNumber(request.getSeatNumber())
                .seatType(seatTypeFromProto(request.getSeatType())) // Maps SeatType from proto to enum
                .build();
    }

    // Maps Seat entity to SeatResponse
    public static SeatResponse toResponse(Seat seat) {
        if (seat == null) {
            return SeatResponse.getDefaultInstance(); // Return default if seat is null
        }
        return SeatResponse.newBuilder()
                .setId(seat.getId().toString())
                .setSeatNumber(seat.getSeatNumber())
                .setSeatType(seatTypeFromEntity(seat.getSeatType())) // Maps SeatType from entity to proto enum
                .setAuditoriumId(seat.getAuditorium().getId().toString())
                .build();
    }

    // Maps list of Seat entities to list of SeatResponse
    public static List<SeatResponse> toResponses(List<Seat> seats) {
        return seats.stream()
                .map(SeatMapper::toResponse)
                .collect(Collectors.toList()); // Explicitly using collect() for clarity
    }
    // Converts SeatType from proto to entity
    public static SeatType seatTypeFromProto(com.bingebox.commons.venue.service.SeatType protoType) {
        if (protoType == null || protoType == com.bingebox.commons.venue.service.SeatType.UNRECOGNIZED) {
            throw new IllegalArgumentException("Invalid or unrecognized SeatType from proto: " + protoType);
        }
        return SeatType.valueOf(protoType.name().toUpperCase());
    }

    // Converts SeatType from entity to proto
    public static com.bingebox.commons.venue.service.SeatType seatTypeFromEntity(SeatType entityType) {
        if (entityType == null) {
            return com.bingebox.commons.venue.service.SeatType.UNRECOGNIZED;
        }
        return com.bingebox.commons.venue.service.SeatType.valueOf(entityType.name().toUpperCase());
    }
}
