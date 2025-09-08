package br.com.productshop.marketplace_api.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseTest {

    @Test
    void shouldCreateProductResponseWithAllFields() {
        // Arrange
        String id = "1";
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = BigDecimal.valueOf(99.99);
        String brand = "Test Brand";
        String category = "Test Category";
        Integer stock = 10;
        List<String> images = Arrays.asList("image1.jpg", "image2.jpg");
        Map<String, String> specifications = new HashMap<String, String>() {{
            put("color", "Red");
            put("size", "M");
        }};
        String sellerId = "seller1";
        Double rating = 4.5;
        Integer totalReviews = 2;
        List<QuestionResponse> questions = Arrays.asList(
            QuestionResponse.builder().id("q1").question("Test Question").build()
        );
        List<ReviewResponse> reviews = Arrays.asList(
            ReviewResponse.builder().id("r1").rating(5).build(),
            ReviewResponse.builder().id("r2").rating(4).build()
        );

        // Act
        ProductResponse response = ProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .brand(brand)
                .category(category)
                .stock(stock)
                .images(images)
                .specifications(specifications)
                .sellerId(sellerId)
                .rating(rating)
                .totalReviews(totalReviews)
                .questions(questions)
                .reviews(reviews)
                .build();

        // Assert
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(description, response.getDescription());
        assertEquals(price, response.getPrice());
        assertEquals(brand, response.getBrand());
        assertEquals(category, response.getCategory());
        assertEquals(stock, response.getStock());
        assertEquals(images, response.getImages());
        assertEquals(specifications, response.getSpecifications());
        assertEquals(sellerId, response.getSellerId());
        assertEquals(rating, response.getRating());
        assertEquals(totalReviews, response.getTotalReviews());
        assertEquals(questions, response.getQuestions());
        assertEquals(reviews, response.getReviews());
    }

    @Test
    void shouldCreateEmptyProductResponse() {
        // Act
        ProductResponse response = new ProductResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getDescription());
        assertNull(response.getPrice());
        assertNull(response.getBrand());
        assertNull(response.getCategory());
        assertNull(response.getStock());
        assertNull(response.getImages());
        assertNull(response.getSpecifications());
        assertNull(response.getSellerId());
        assertNull(response.getRating());
        assertNull(response.getTotalReviews());
        assertNull(response.getQuestions());
        assertNull(response.getReviews());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        ProductResponse response1 = ProductResponse.builder()
                .id("1")
                .name("Product 1")
                .price(BigDecimal.TEN)
                .build();

        ProductResponse response2 = ProductResponse.builder()
                .id("1")
                .name("Product 1")
                .price(BigDecimal.TEN)
                .build();

        ProductResponse response3 = ProductResponse.builder()
                .id("2")
                .name("Product 2")
                .price(BigDecimal.ONE)
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
        ProductResponse response = ProductResponse.builder()
                .id("1")
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .build();

        // Act
        String toString = response.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Test Product"));
        assertTrue(toString.contains("price=99.99"));
    }
}
