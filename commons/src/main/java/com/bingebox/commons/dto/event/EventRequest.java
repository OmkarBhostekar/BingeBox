package com.bingebox.commons.dto.event;

public record EventRequest(
    String title,
    String description,
    String category,
    String language,
    String ageRating,
    String bannerUrl
) { }