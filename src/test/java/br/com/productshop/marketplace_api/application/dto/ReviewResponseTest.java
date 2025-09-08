package br.com.productshop.marketplace_api.application.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ReviewResponseTest {

    @Test
    void shouldCreateReviewResponseWithAllFields() {
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
        ReviewResponse response = ReviewResponse.builder()
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
        assertEquals(id, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(userName, response.getUserName());
        assertEquals(rating, response.getRating());
        assertEquals(comment, response.getComment());
        assertEquals(reviewDate, response.getReviewDate());
        assertEquals(verifiedPurchase, response.getVerifiedPurchase());
        assertEquals(likes, response.getLikes());
    }

    @Test
    void shouldCreateEmptyReviewResponse() {
        // Act
        ReviewResponse response = new ReviewResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getUserId());
        assertNull(response.getUserName());
        assertNull(response.getRating());
        assertNull(response.getComment());
        assertNull(response.getReviewDate());
        assertNull(response.getVerifiedPurchase());
        assertNull(response.getLikes());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        ReviewResponse response1 = ReviewResponse.builder()
                .id("1")
                .userName("John")
                .rating(5)
                .build();

        ReviewResponse response2 = ReviewResponse.builder()
                .id("1")
                .userName("John")
                .rating(5)
                .build();

        ReviewResponse response3 = ReviewResponse.builder()
                .id("2")
                .userName("Jane")
                .rating(4)
                .build();

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        ReviewResponse response = ReviewResponse.builder()
                .id("1")
                .userName("John")
                .rating(5)
                .comment("Great!")
                .build();

        // Act
        String toString = response.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userName=John"));
        assertTrue(toString.contains("rating=5"));
        assertTrue(toString.contains("comment=Great!"));
    }

    @Test
    void shouldHandleNullFields() {
        // Act
        ReviewResponse response = ReviewResponse.builder()
                .id("1")
                .rating(5)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals(5, response.getRating());
        assertNull(response.getUserName());
        assertNull(response.getComment());
        assertNull(response.getReviewDate());
    }
}
