package br.com.productshop.marketplace_api.domain.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithAllFields() {
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
        List<Review> reviews = Arrays.asList(
            Review.builder().id("r1").rating(5).build(),
            Review.builder().id("r2").rating(4).build()
        );
        List<Question> questions = Arrays.asList(
            Question.builder().id("q1").question("Test Question").build()
        );

        // Act
        Product product = Product.builder()
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
                .reviews(reviews)
                .questions(questions)
                .build();

        // Assert
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(brand, product.getBrand());
        assertEquals(category, product.getCategory());
        assertEquals(stock, product.getStock());
        assertEquals(images, product.getImages());
        assertEquals(specifications, product.getSpecifications());
        assertEquals(sellerId, product.getSellerId());
        assertEquals(rating, product.getRating());
        assertEquals(reviews, product.getReviews());
        assertEquals(questions, product.getQuestions());
    }

    @Test
    void shouldCreateEmptyProduct() {
        // Act
        Product product = new Product();

        // Assert
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
        assertNull(product.getBrand());
        assertNull(product.getCategory());
        assertNull(product.getStock());
        assertNull(product.getImages());
        assertNull(product.getSpecifications());
        assertNull(product.getSellerId());
        assertNull(product.getRating());
        assertNull(product.getReviews());
        assertNull(product.getQuestions());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        Product product1 = Product.builder().id("1").name("Product 1").build();
        Product product2 = Product.builder().id("1").name("Product 1").build();
        Product product3 = Product.builder().id("2").name("Product 2").build();

        // Assert
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(BigDecimal.TEN)
                .build();

        // Act
        String toString = product.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Test Product"));
        assertTrue(toString.contains("price=10"));
    }
}
