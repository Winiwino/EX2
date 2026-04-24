package com.example.guide.dto.response;

import com.example.guide.entity.AttractionCategory;

public record AttractionResponse(
        Long id,
        String name,
        AttractionCategory category,
        double latitude,
        double longitude,
        Double averageRating,
        long ratingsCount
) {
}
