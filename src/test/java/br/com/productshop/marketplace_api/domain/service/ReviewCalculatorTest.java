package br.com.productshop.marketplace_api.domain.service;

import br.com.productshop.marketplace_api.domain.entity.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ReviewCalculatorTest {

    @InjectMocks
    private ReviewCalculator reviewCalculator;

    @Test
    void shouldReturnZeroWhenReviewListIsNull() {
        assertEquals(0.0, reviewCalculator.calculateAverageRating(null));
    }

    @Test
    void shouldReturnZeroWhenReviewListIsEmpty() {
        assertEquals(0.0, reviewCalculator.calculateAverageRating(Collections.emptyList()));
    }

    @Test
    void shouldCalculateAverageCorrectly() {
        Review review1 = Review.builder().rating(5).build();
        Review review2 = Review.builder().rating(4).build();
        Review review3 = Review.builder().rating(4).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2, review3));
        assertEquals(4.3, average);
    }

    @Test
    void shouldRoundToOneDecimalPlace() {
        Review review1 = Review.builder().rating(5).build();
        Review review2 = Review.builder().rating(3).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2));
        assertEquals(4.0, average);
    }

    @Test
    void shouldCalculateAverageWithSingleReview() {
        Review review = Review.builder().rating(4).build();

        double average = reviewCalculator.calculateAverageRating(Collections.singletonList(review));
        assertEquals(4.0, average);
    }

    @Test
    void shouldCalculateAverageWithAllFiveStars() {
        Review review1 = Review.builder().rating(5).build();
        Review review2 = Review.builder().rating(5).build();
        Review review3 = Review.builder().rating(5).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2, review3));
        assertEquals(5.0, average);
    }

    @Test
    void shouldCalculateAverageWithAllOneStars() {
        Review review1 = Review.builder().rating(1).build();
        Review review2 = Review.builder().rating(1).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2));
        assertEquals(1.0, average);
    }

    @Test
    void shouldCalculateAverageWithMixedRatings() {
        Review review1 = Review.builder().rating(5).build();
        Review review2 = Review.builder().rating(1).build();
        Review review3 = Review.builder().rating(3).build();
        Review review4 = Review.builder().rating(4).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2, review3, review4));
        assertEquals(3.3, average);
    }

    @Test
    void shouldRoundDownToOneDecimalPlace() {
        Review review1 = Review.builder().rating(3).build();
        Review review2 = Review.builder().rating(4).build();
        Review review3 = Review.builder().rating(4).build();

        double average = reviewCalculator.calculateAverageRating(Arrays.asList(review1, review2, review3));
        assertEquals(3.7, average);
    }
}
