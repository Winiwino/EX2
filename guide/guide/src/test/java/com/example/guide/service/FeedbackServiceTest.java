package com.example.guide.service;

import com.example.guide.entity.Attraction;
import com.example.guide.entity.AttractionCategory;
import com.example.guide.entity.Rating;
import com.example.guide.entity.Review;
import com.example.guide.exception.NotFoundException;
import com.example.guide.repository.AttractionRepository;
import com.example.guide.repository.RatingRepository;
import com.example.guide.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        feedbackService = new FeedbackService(attractionRepository, ratingRepository, reviewRepository);
    }

    @Test
    void addRating() {
        Attraction attraction = new Attraction(1L, "Ленинский мемориал", AttractionCategory.MUSEUM, 54.3142, 48.4031);
        Rating savedRating = new Rating(7L, attraction, "Никита", 5);

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(ratingRepository.save(any(Rating.class))).thenReturn(savedRating);

        Rating result = feedbackService.addRating(1L, "Никита", 5);

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(captor.capture());
        assertThat(captor.getValue().getAttraction()).isEqualTo(attraction);
        assertThat(captor.getValue().getAuthor()).isEqualTo("Никита");
        assertThat(captor.getValue().getScore()).isEqualTo(5);
        assertThat(result.getId()).isEqualTo(7L);
    }

    @Test
    void addReview() {
        Attraction attraction = new Attraction(3L, "Бульвар Новый Венец", AttractionCategory.VIEWPOINT, 54.3148, 48.4020);
        Review savedReview = new Review(9L, attraction, "Денис", "Лучшее место для прогулки.", Instant.now());

        when(attractionRepository.findById(3L)).thenReturn(Optional.of(attraction));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        Review result = feedbackService.addReview(3L, "Денис", "Лучшее место для прогулки.");

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());
        assertThat(captor.getValue().getAttraction()).isEqualTo(attraction);
        assertThat(captor.getValue().getAuthor()).isEqualTo("Денис");
        assertThat(captor.getValue().getText()).isEqualTo("Лучшее место для прогулки.");
        assertThat(result.getId()).isEqualTo(9L);
    }

    @Test
    void getReviews() {
        Attraction attraction = new Attraction(4L, "Парк Победы", AttractionCategory.PARK, 54.3380, 48.3795);
        Review first = new Review(1L, attraction, "Стас", "Большой парк.", Instant.parse("2026-04-24T10:15:30Z"));
        Review second = new Review(2L, attraction, "Глеб", "Хорошее место.", Instant.parse("2026-04-23T09:15:30Z"));

        when(attractionRepository.findById(4L)).thenReturn(Optional.of(attraction));
        when(reviewRepository.findByAttractionIdOrderByCreatedAtDesc(4L)).thenReturn(List.of(first, second));

        List<Review> result = feedbackService.getReviews(4L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Review::getAuthor).containsExactly("Стас", "Глеб");
    }

    @Test
    void addRatingNotFound() {
        when(attractionRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.addRating(77L, "Никита", 5))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Достопримечательность с id=77 не найдена");
    }
}
