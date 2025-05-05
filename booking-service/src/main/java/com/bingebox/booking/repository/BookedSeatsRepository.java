package com.bingebox.booking.repository;

import com.bingebox.booking.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookedSeatsRepository extends JpaRepository<BookedSeat, UUID> {
    boolean existsBySeatIdAndEventShowId(String seatId, String eventShowId);
    List<BookedSeat> findByBookingId(UUID bookingId);
}
