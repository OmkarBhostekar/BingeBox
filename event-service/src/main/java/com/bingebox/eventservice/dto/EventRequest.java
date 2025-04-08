package com.bingebox.eventservice.dto;

public record EventRequest(
    String title,
    String description,
    String category,
    String language,
    String ageRating,
    String bannerUrl
) { }