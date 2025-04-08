package com.bingebox.eventservice.repository;

import com.bingebox.eventservice.model.EventShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventShowRepository extends JpaRepository<EventShow, UUID> {
}
