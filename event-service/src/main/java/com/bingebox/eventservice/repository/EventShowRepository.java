package com.bingebox.eventservice.repository;

import com.bingebox.eventservice.model.EventShow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventShowRepository extends JpaRepository<EventShow, UUID> {

    List<EventShow> findByEventId(UUID eventId);

    List<EventShow> findByEventIdAndAuditoriumId(UUID eventId, String auditoriumId);

    List<EventShow> findByEventIdAndScheduledTimeBetween(UUID eventId, Instant startOfDay, Instant endOfDay);

    List<EventShow> findByAuditoriumIdAndScheduledTimeBetween(String auditoriumId, Instant startOfDay, Instant endOfDay);

    List<EventShow> findByEventIdAndAuditoriumIdAndScheduledTimeBetween(UUID eventId, String auditoriumId, Instant startOfDay, Instant endOfDay);

    List<EventShow> findByScheduledTimeBetween(Instant startOfDay, Instant endOfDay);
}
