package br.com.productshop.marketplace_api.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void shouldCreateReviewWithAllFields() {
        // Arrange
        String id = "r1";
        String userId = "user1";
        String userName = "John Doe";
        Integer rating = 5;
        String comment = "Great product!";
        LocalDateTime reviewDate = LocalDateTime.now();
        Boolean verifiedPurchase = true;
        Integer likes = 10;

        // Act
        Review review = Review.builder()
                .id(id)
                .userId(userId)
                .userName(userName)
                .rating(rating)
                .comment(comment)
                .reviewDate(reviewDate)
                .verifiedPurchase(verifiedPurchase)
                .likes(likes)
                .build();

        // Assert
        assertEquals(id, review.getId());
        assertEquals(userId, review.getUserId());
        assertEquals(userName, review.getUserName());
        assertEquals(rating, review.getRating());
        assertEquals(comment, review.getComment());
        assertEquals(reviewDate, review.getReviewDate());
        assertEquals(verifiedPurchase, review.getVerifiedPurchase());
        assertEquals(likes, review.getLikes());
    }

    @Test
    void shouldCreateEmptyReview() {
        // Act
        Review review = new Review();

        // Assert
        assertNotNull(review);
        assertNull(review.getId());
        assertNull(review.getUserId());
        assertNull(review.getUserName());
        assertNull(review.getRating());
        assertNull(review.getComment());
        assertNull(review.getReviewDate());
        assertNull(review.getVerifiedPurchase());
        assertNull(review.getLikes());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        Review review1 = Review.builder().id("1").rating(5).build();
        Review review2 = Review.builder().id("1").rating(5).build();
        Review review3 = Review.builder().id("2").rating(4).build();

        // Assert
        assertEquals(review1, review2);
        assertNotEquals(review1, review3);
        assertEquals(review1.hashCode(), review2.hashCode());
        assertNotEquals(review1.hashCode(), review3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        Review review = Review.builder()
                .id("1")
                .userName("John")
                .rating(5)
                .build();

        // Act
        String toString = review.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userName=John"));
        assertTrue(toString.contains("rating=5"));
    }
}
