package com.bingebox.venueservice.repository;

import com.bingebox.venueservice.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {
}
