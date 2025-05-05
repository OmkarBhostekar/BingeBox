package com.bingebox.booking.mapper;

import com.bingebox.booking.model.BookedSeat;
import com.bingebox.booking.model.Booking;
import com.bingebox.commons.dto.booking.BookingResponseDTO;
import com.bingebox.commons.dto.booking.BookingSeatDTO;
import com.bingebox.commons.dto.event.EventShowResponse;

import java.util.List;

public class BookingMapper {

    public static BookingResponseDTO toResponse(Booking booking, List<BookedSeat> bookedSeats, EventShowResponse eventShowResponse) {
        return new BookingResponseDTO(
            eventShowResponse,
            bookedSeats.stream().map(BookingMapper::toBookingSeatDTO).toList(),
            booking.getUserId()
        );
    }

    public static BookingSeatDTO toBookingSeatDTO(BookedSeat bookedSeat) {
        return new BookingSeatDTO(
            bookedSeat.getSeatId(),
            bookedSeat.getSeatType().name().toUpperCase(),
            bookedSeat.getSeatNumber(),
            bookedSeat.getStatus().name().toUpperCase()
        );
    }
}
