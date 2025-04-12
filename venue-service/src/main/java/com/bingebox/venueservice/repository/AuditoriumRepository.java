package com.bingebox.venueservice.repository;

import com.bingebox.venueservice.model.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditoriumRepository extends JpaRepository<Auditorium, UUID> {
}
