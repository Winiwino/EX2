package com.example.guide.controller;

import com.example.guide.dto.request.CreateRatingRequest;
import com.example.guide.dto.request.CreateReviewRequest;
import com.example.guide.dto.response.AttractionResponse;
import com.example.guide.dto.response.NearbyAttractionResponse;
import com.example.guide.dto.response.ReviewResponse;
import com.example.guide.dto.response.SubmissionResponse;
import com.example.guide.entity.AttractionCategory;
import com.example.guide.mapper.AttractionMapper;
import com.example.guide.mapper.ReviewMapper;
import com.example.guide.service.AttractionSearchCriteria;
import com.example.guide.service.AttractionService;
import com.example.guide.service.AttractionSortBy;
import com.example.guide.service.FeedbackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    private final AttractionService attractionService;
    private final FeedbackService feedbackService;
    private final AttractionMapper attractionMapper;
    private final ReviewMapper reviewMapper;

    public AttractionController(
            AttractionService attractionService,
            FeedbackService feedbackService,
            AttractionMapper attractionMapper,
            ReviewMapper reviewMapper
    ) {
        this.attractionService = attractionService;
        this.feedbackService = feedbackService;
        this.attractionMapper = attractionMapper;
        this.reviewMapper = reviewMapper;
    }

    @GetMapping("/search")
    public List<NearbyAttractionResponse> search(
            @RequestParam @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") double latitude,
            @RequestParam @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") double longitude,
            @RequestParam @DecimalMin(value = "0.0", inclusive = false) double radiusMeters,
            @RequestParam(required = false) AttractionCategory category,
            @RequestParam(required = false) @DecimalMin(value = "1.0") @DecimalMax(value = "5.0") Double minAverageRating,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit,
            @RequestParam(defaultValue = "DISTANCE") AttractionSortBy sortBy
    ) {
        AttractionSearchCriteria criteria = new AttractionSearchCriteria(
                latitude,
                longitude,
                radiusMeters,
                category,
                minAverageRating,
                limit,
                sortBy
        );

        return attractionService.search(criteria).stream()
                .map(attractionMapper::toNearbyResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AttractionResponse getById(@PathVariable Long id) {
        return attractionMapper.toAttractionResponse(attractionService.getAttraction(id));
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewResponse> getReviews(@PathVariable Long id) {
        return feedbackService.getReviews(id).stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @PostMapping("/{id}/ratings")
    public SubmissionResponse addRating(@PathVariable Long id, @Valid @RequestBody CreateRatingRequest request) {
        Long ratingId = feedbackService.addRating(id, request.author(), request.score()).getId();
        return new SubmissionResponse(ratingId, "Оценка сохранена");
    }

    @PostMapping("/{id}/reviews")
    public SubmissionResponse addReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request) {
        Long reviewId = feedbackService.addReview(id, request.author(), request.text()).getId();
        return new SubmissionResponse(reviewId, "Отзыв сохранен");
    }
}
