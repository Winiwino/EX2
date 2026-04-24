package com.example.guide.mapper;

import com.example.guide.dto.response.AttractionResponse;
import com.example.guide.dto.response.NearbyAttractionResponse;
import com.example.guide.service.AttractionSummary;
import org.springframework.stereotype.Component;

@Component
public class AttractionMapper {

    public NearbyAttractionResponse toNearbyResponse(AttractionSummary summary) {
        return new NearbyAttractionResponse(
                summary.attraction().getId(),
                summary.attraction().getName(),
                summary.attraction().getCategory(),
                summary.attraction().getLatitude(),
                summary.attraction().getLongitude(),
                summary.averageRating(),
                summary.ratingsCount(),
                summary.distanceMeters()
        );
    }

    public AttractionResponse toAttractionResponse(AttractionSummary summary) {
        return new AttractionResponse(
                summary.attraction().getId(),
                summary.attraction().getName(),
                summary.attraction().getCategory(),
                summary.attraction().getLatitude(),
                summary.attraction().getLongitude(),
                summary.averageRating(),
                summary.ratingsCount()
        );
    }
}
