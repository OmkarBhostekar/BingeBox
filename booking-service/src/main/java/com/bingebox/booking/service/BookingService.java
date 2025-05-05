package com.bingebox.booking.service;

import com.bingebox.booking.mapper.BookingMapper;
import com.bingebox.booking.model.BookedSeat;
import com.bingebox.booking.model.Booking;
import com.bingebox.booking.repository.BookedSeatsRepository;
import com.bingebox.booking.repository.BookingRepository;
import com.bingebox.booking.utils.RestGateway;
import com.bingebox.commons.dto.booking.BookingRequestDTO;
import com.bingebox.commons.dto.booking.BookingResponseDTO;
import com.bingebox.commons.dto.booking.BookingSeatDTO;
import com.bingebox.commons.dto.event.EventShowResponse;
import com.bingebox.commons.enums.BookingStatus;
import com.bingebox.commons.enums.SeatStatus;
import com.bingebox.commons.exceptions.EventShowNotFoundException;
import com.bingebox.commons.exceptions.SeatAlreadyBookedException;
import com.bingebox.commons.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookedSeatsRepository bookedSeatsRepository;
    private final RestGateway restGateway;

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        // Check if the event show exists
        EventShowResponse eventShowResponse = restGateway.get(Constants.EVENT_SERVICE_URL, "/shows", EventShowResponse.class, bookingRequest.eventShowId());
        if (eventShowResponse == null) {
            throw new EventShowNotFoundException("Event show not found");
        }

        // Check if the seats are available
        List<BookedSeat> bookedSeats = new ArrayList<>();
        for (BookingSeatDTO seat : bookingRequest.bookingSeats()) {
            if (bookedSeatsRepository.existsBySeatIdAndEventShowId(seat.seatId(), bookingRequest.eventShowId())) {
                throw new SeatAlreadyBookedException("Seat " + seat.seatId() + " is already booked");
            }

            // Save the booked seats as ON_HOLD
            BookedSeat bookedSeat = BookedSeat.builder()
                    .id(UUID.randomUUID())
                    .bookingId(null) // To be set once booking is saved
                    .eventShowId(bookingRequest.eventShowId())
                    .seatId(seat.seatId())
                    .status(SeatStatus.ON_HOLD)
                    .build();
            bookedSeats.add(bookedSeat);
        }

        // Create a new booking
        Booking booking = Booking.builder()
                .id(UUID.randomUUID())
                .bookingStatus(BookingStatus.PENDING)
                .eventShowId(bookingRequest.eventShowId())
                .userId(bookingRequest.userId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // Save the booking to the database
        booking = bookingRepository.save(booking);

        // Now, update booked seats with the booking ID
        for (BookedSeat bookedSeat : bookedSeats) {
            bookedSeat.setBookingId(booking.getId());
            bookedSeatsRepository.save(bookedSeat);
        }

        // Publish event to notify the payment service and communication service for notifications
        // Implement event publishing here, e.g., Kafka, RabbitMQ, etc.

        // Return the booking response
        return BookingMapper.toResponse(booking, bookedSeats, eventShowResponse);
    }

    @Transactional
    public BookingResponseDTO confirmBooking(UUID bookingId) {
        // Check if the event show exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        EventShowResponse eventShowResponse = restGateway.get(Constants.EVENT_SERVICE_URL, "/shows", EventShowResponse.class, booking.getEventShowId());
        if (eventShowResponse == null) {
            throw new EventShowNotFoundException("Event show not found");
        }

        // Update the booking status to CONFIRMED
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(Instant.now());
        bookingRepository.save(booking); // Save the updated booking

        // Update the booked seats status to BOOKED
        List<BookedSeat> bookedSeats = bookedSeatsRepository.findByBookingId(bookingId);
        bookedSeats.forEach(bookedSeat -> {
            bookedSeat.setStatus(SeatStatus.BOOKED);
            bookedSeatsRepository.save(bookedSeat); // Save the updated booked seat
        });

        // Publish event to notify the user about booking confirmation Email / SMS

        // Return the updated booking response
        return BookingMapper.toResponse(booking, bookedSeats, eventShowResponse);
    }

    @Transactional
    public String cancelBooking(UUID bookingId) {
        // Fetch the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Check the booking status to ensure it can be cancelled
        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Cannot cancel a confirmed booking");
        }

        // Update the booking status to CANCELLED
        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(Instant.now());
        bookingRepository.save(booking);

        // Update the booked seats status to CANCELLED
        List<BookedSeat> bookedSeats = bookedSeatsRepository.findByBookingId(bookingId);
        bookedSeats.forEach(bookedSeat -> {
            bookedSeat.setStatus(SeatStatus.UNAVAILABLE);
            bookedSeatsRepository.save(bookedSeat);
        });

        // Return cancellation message
        return "Booking cancelled successfully";
    }

    public BookingResponseDTO getBookingDetails(UUID bookingId) {
        // Fetch the booking from the repository
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Fetch the booked seats related to the booking
        List<BookedSeat> bookedSeats = bookedSeatsRepository.findByBookingId(bookingId);

        // Fetch the event show details for the booking
        EventShowResponse eventShowResponse = restGateway.get(Constants.EVENT_SERVICE_URL, "/shows", EventShowResponse.class, booking.getEventShowId());
        if (eventShowResponse == null) {
            throw new EventShowNotFoundException("Event show not found");
        }

        // Return the booking details as response DTO
        return BookingMapper.toResponse(booking, bookedSeats, eventShowResponse);
    }

    public List<BookingResponseDTO> getUserBookings(String userId) {
        // Fetch the bookings by user ID
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        // Convert the list of bookings to response DTOs
        List<BookingResponseDTO> response = new ArrayList<>();
        for (Booking booking : bookings) {
            // Fetch the booked seats related to the booking
            List<BookedSeat> bookedSeats = bookedSeatsRepository.findByBookingId(booking.getId());

            // Fetch the event show details for the booking
            EventShowResponse eventShowResponse = restGateway.get(Constants.EVENT_SERVICE_URL, "/shows", EventShowResponse.class, booking.getEventShowId());
            if (eventShowResponse == null) {
                throw new EventShowNotFoundException("Event show not found");
            }

            // Add the converted booking response DTO to the list
            response.add(BookingMapper.toResponse(booking, bookedSeats, eventShowResponse));
        }

        return response;
    }
}

