package br.com.productshop.marketplace_api.domain.service;

import br.com.productshop.marketplace_api.domain.entity.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewCalculator {

    public double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();

        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}
