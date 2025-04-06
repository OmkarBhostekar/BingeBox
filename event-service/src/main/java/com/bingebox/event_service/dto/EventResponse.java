package com.bingebox.event_service.dto;

public record EventResponse(
    String id,
    String title,
    String description,
    String category,
    String language,
    String ageRating,
    String bannerUrl,
    String createdAt
) {}
