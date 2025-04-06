package com.bingebox.event_service.dto;

public record EventRequest(
    String title,
    String description,
    String category,
    String language,
    String ageRating,
    String bannerUrl
) { }