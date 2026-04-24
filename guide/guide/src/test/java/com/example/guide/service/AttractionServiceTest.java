package com.example.guide.service;

import com.example.guide.entity.Attraction;
import com.example.guide.entity.AttractionCategory;
import com.example.guide.exception.NotFoundException;
import com.example.guide.repository.AttractionRepository;
import com.example.guide.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private RatingRepository ratingRepository;

    private AttractionService attractionService;

    @BeforeEach
    void setUp() {
        attractionService = new AttractionService(attractionRepository, ratingRepository);
    }

    @Test
    void searchByRating() {
        Attraction memorial = new Attraction(1L, "Ленинский мемориал", AttractionCategory.MUSEUM, 54.3142, 48.4031);
        Attraction venets = new Attraction(3L, "Бульвар Новый Венец", AttractionCategory.VIEWPOINT, 54.3148, 48.4020);
        Attraction museum = new Attraction(5L, "Ульяновский областной краеведческий музей", AttractionCategory.MUSEUM, 54.3145, 48.4044);

        when(attractionRepository.findAll()).thenReturn(List.of(memorial, venets, museum));
        when(ratingRepository.findAverageScoreByAttractionId(1L)).thenReturn(5.0);
        when(ratingRepository.findAverageScoreByAttractionId(5L)).thenReturn(5.0);
        when(ratingRepository.countByAttractionId(1L)).thenReturn(1L);
        when(ratingRepository.countByAttractionId(5L)).thenReturn(1L);

        AttractionSearchCriteria criteria = new AttractionSearchCriteria(
                54.3140,
                48.4030,
                250,
                AttractionCategory.MUSEUM,
                5.0,
                5,
                AttractionSortBy.RATING
        );

        List<AttractionSummary> result = attractionService.search(criteria);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(summary -> summary.attraction().getId()).containsExactly(1L, 5L);
    }

    @Test
    void getById() {
        Attraction attraction = new Attraction(10L, "Набережная реки Волги", AttractionCategory.VIEWPOINT, 54.3181, 48.4062);

        when(attractionRepository.findById(10L)).thenReturn(Optional.of(attraction));
        when(ratingRepository.findAverageScoreByAttractionId(10L)).thenReturn(null);
        when(ratingRepository.countByAttractionId(10L)).thenReturn(0L);

        AttractionSummary result = attractionService.getAttraction(10L);

        assertThat(result.attraction().getName()).isEqualTo("Набережная реки Волги");
        assertThat(result.averageRating()).isNull();
        assertThat(result.ratingsCount()).isZero();
        assertThat(result.distanceMeters()).isZero();
    }

    @Test
    void getByIdNotFound() {
        when(attractionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attractionService.getAttraction(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Достопримечательность с id=99 не найдена");
    }
}
