package com.example.guide.service;

import com.example.guide.entity.Attraction;

public record AttractionSummary(
        Attraction attraction,
        Double averageRating,
        long ratingsCount,
        double distanceMeters
) {
}
