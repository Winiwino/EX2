package com.example.guide.service;

import com.example.guide.entity.AttractionCategory;

public record AttractionSearchCriteria(
        double latitude,
        double longitude,
        double radiusMeters,
        AttractionCategory category,
        Double minAverageRating,
        int limit,
        AttractionSortBy sortBy
) {
}
