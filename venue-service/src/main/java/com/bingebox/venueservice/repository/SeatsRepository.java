package com.bingebox.venueservice.repository;

import com.bingebox.venueservice.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatsRepository extends JpaRepository<Seat, UUID> {

    // Custom query to find all seats by auditorium ID
    List<Seat> findByAuditorium_Id(UUID auditoriumId);
}
