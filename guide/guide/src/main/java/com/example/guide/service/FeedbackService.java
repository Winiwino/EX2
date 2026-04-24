package com.example.guide.service;

import com.example.guide.entity.Attraction;
import com.example.guide.entity.Rating;
import com.example.guide.entity.Review;
import com.example.guide.exception.NotFoundException;
import com.example.guide.repository.AttractionRepository;
import com.example.guide.repository.RatingRepository;
import com.example.guide.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    private final AttractionRepository attractionRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;

    public FeedbackService(
            AttractionRepository attractionRepository,
            RatingRepository ratingRepository,
            ReviewRepository reviewRepository
    ) {
        this.attractionRepository = attractionRepository;
        this.ratingRepository = ratingRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Rating addRating(Long attractionId, String author, int score) {
        Attraction attraction = getAttraction(attractionId);
        return ratingRepository.save(new Rating(null, attraction, author, score));
    }

    @Transactional
    public Review addReview(Long attractionId, String author, String text) {
        Attraction attraction = getAttraction(attractionId);
        return reviewRepository.save(new Review(null, attraction, author, text, null));
    }

    @Transactional(readOnly = true)
    public List<Review> getReviews(Long attractionId) {
        getAttraction(attractionId);
        return reviewRepository.findByAttractionIdOrderByCreatedAtDesc(attractionId);
    }

    private Attraction getAttraction(Long attractionId) {
        return attractionRepository.findById(attractionId)
                .orElseThrow(() -> new NotFoundException("Достопримечательность с id=" + attractionId + " не найдена"));
    }
}
