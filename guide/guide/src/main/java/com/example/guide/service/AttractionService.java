package com.example.guide.service;

import com.example.guide.entity.Attraction;
import com.example.guide.exception.NotFoundException;
import com.example.guide.repository.AttractionRepository;
import com.example.guide.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final RatingRepository ratingRepository;

    public AttractionService(AttractionRepository attractionRepository, RatingRepository ratingRepository) {
        this.attractionRepository = attractionRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<AttractionSummary> search(AttractionSearchCriteria criteria) {
        return attractionRepository.findAll().stream()
                .filter(attraction -> criteria.category() == null || attraction.getCategory() == criteria.category())
                .map(attraction -> toSummary(attraction, criteria.latitude(), criteria.longitude()))
                .filter(summary -> summary.distanceMeters() <= criteria.radiusMeters())
                .filter(summary -> criteria.minAverageRating() == null
                        || (summary.averageRating() != null && summary.averageRating() >= criteria.minAverageRating()))
                .sorted(resolveComparator(criteria.sortBy()))
                .limit(criteria.limit())
                .toList();
    }

    public AttractionSummary getAttraction(Long attractionId) {
        Attraction attraction = attractionRepository.findById(attractionId)
                .orElseThrow(() -> new NotFoundException("Достопримечательность с id=" + attractionId + " не найдена"));
        return toSummary(attraction, attraction.getLatitude(), attraction.getLongitude());
    }

    private AttractionSummary toSummary(Attraction attraction, double latitude, double longitude) {
        Double averageRating = ratingRepository.findAverageScoreByAttractionId(attraction.getId());
        long ratingsCount = ratingRepository.countByAttractionId(attraction.getId());
        double distance = GeoUtils.distanceMeters(latitude, longitude, attraction.getLatitude(), attraction.getLongitude());
        return new AttractionSummary(attraction, averageRating, ratingsCount, distance);
    }

    private Comparator<AttractionSummary> resolveComparator(AttractionSortBy sortBy) {
        return switch (sortBy) {
            case NAME -> Comparator.comparing(summary -> summary.attraction().getName(), String.CASE_INSENSITIVE_ORDER);
            case RATING -> Comparator
                    .comparing((AttractionSummary summary) -> summary.averageRating() == null ? -1.0 : summary.averageRating())
                    .reversed()
                    .thenComparingDouble(AttractionSummary::distanceMeters);
            case DISTANCE -> Comparator.comparingDouble(AttractionSummary::distanceMeters);
        };
    }
}
