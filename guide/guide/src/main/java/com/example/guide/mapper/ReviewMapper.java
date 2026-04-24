package com.example.guide.mapper;

import com.example.guide.dto.response.ReviewResponse;
import com.example.guide.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(review.getId(), review.getAuthor(), review.getText(), review.getCreatedAt());
    }
}
