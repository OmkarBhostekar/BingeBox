package com.bingebox.booking.controller;

import com.bingebox.commons.dto.booking.BookingRequestDTO;
import com.bingebox.booking.service.BookingService;
import com.bingebox.commons.dto.booking.BookingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequest) {
        // Call the service layer to create the booking
        BookingResponseDTO response = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@RequestBody UUID bookingId) {
        // Confirm the booking by passing the UUID to the service layer
        BookingResponseDTO response = bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestBody UUID bookingId) {
        // Implement the logic for cancelling the booking
        String cancellationMessage = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(cancellationMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingDetails(@PathVariable UUID id) {
        // Fetch the booking details using the booking ID
        BookingResponseDTO bookingDetails = bookingService.getBookingDetails(id);
        return ResponseEntity.ok(bookingDetails);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getUserBookings(@PathVariable String userId) {
        // Fetch the user's bookings
        List<BookingResponseDTO> userBookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(userBookings);
    }

}

