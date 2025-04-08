package com.bingebox.eventservice.repository;

import com.bingebox.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
