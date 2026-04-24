package com.example.guide.dto.response;

import java.time.Instant;

public record ReviewResponse(
        Long id,
        String author,
        String text,
        Instant createdAt
) {
}
