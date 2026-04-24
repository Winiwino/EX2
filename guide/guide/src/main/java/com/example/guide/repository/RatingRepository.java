package com.example.guide.repository;

import com.example.guide.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.score) from Rating r where r.attraction.id = :attractionId")
    Double findAverageScoreByAttractionId(@Param("attractionId") Long attractionId);

    long countByAttractionId(Long attractionId);
}
