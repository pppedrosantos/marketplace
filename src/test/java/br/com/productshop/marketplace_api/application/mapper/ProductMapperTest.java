package br.com.productshop.marketplace_api.application.mapper;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.dto.QuestionResponse;
import br.com.productshop.marketplace_api.application.dto.ReviewResponse;
import br.com.productshop.marketplace_api.domain.entity.Product;
import br.com.productshop.marketplace_api.domain.entity.Question;
import br.com.productshop.marketplace_api.domain.entity.Review;
import br.com.productshop.marketplace_api.domain.service.ReviewCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Mock
    private ReviewCalculator reviewCalculator;

    @InjectMocks
    private ProductMapper productMapper;

    @Test
    void shouldMapProductToResponseWithAllFields() {
        // Arrange
        Product product = createFullProduct();
        when(reviewCalculator.calculateAverageRating(product.getReviews())).thenReturn(4.5);

        // Act
        ProductResponse response = productMapper.toResponse(product);

        // Assert
        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
        assertEquals(product.getBrand(), response.getBrand());
        assertEquals(product.getCategory(), response.getCategory());
        assertEquals(product.getStock(), response.getStock());
        assertEquals(product.getImages(), response.getImages());
        assertEquals(product.getSpecifications(), response.getSpecifications());
        assertEquals(product.getSellerId(), response.getSellerId());
        assertEquals(4.5, response.getRating());
        assertEquals(2, response.getTotalReviews());

        // Verify Questions
        assertNotNull(response.getQuestions());
        assertEquals(1, response.getQuestions().size());
        QuestionResponse questionResponse = response.getQuestions().get(0);
        assertEquals(product.getQuestions().get(0).getId(), questionResponse.getId());

        // Verify Reviews
        assertNotNull(response.getReviews());
        assertEquals(2, response.getReviews().size());
        ReviewResponse reviewResponse = response.getReviews().get(0);
        assertEquals(product.getReviews().get(0).getId(), reviewResponse.getId());
    }

    @Test
    void shouldMapProductWithNullCollections() {
        // Arrange
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(BigDecimal.TEN)
                .build();

        when(reviewCalculator.calculateAverageRating(null)).thenReturn(0.0);

        // Act
        ProductResponse response = productMapper.toResponse(product);

        // Assert
        assertNotNull(response);
        assertNull(response.getQuestions());
        assertNull(response.getReviews());
        assertEquals(0.0, response.getRating());
        assertEquals(0, response.getTotalReviews());
    }

    @Test
    void shouldMapProductWithEmptyCollections() {
        // Arrange
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(BigDecimal.TEN)
                .questions(Collections.emptyList())
                .reviews(Collections.emptyList())
                .build();

        when(reviewCalculator.calculateAverageRating(Collections.emptyList())).thenReturn(0.0);

        // Act
        ProductResponse response = productMapper.toResponse(product);

        // Assert
        assertNotNull(response);
        assertTrue(response.getQuestions().isEmpty());
        assertTrue(response.getReviews().isEmpty());
        assertEquals(0.0, response.getRating());
        assertEquals(0, response.getTotalReviews());
    }

    private Product createFullProduct() {
        Review review1 = Review.builder()
                .id("r1")
                .userId("user1")
                .userName("John Doe")
                .rating(5)
                .comment("Great product!")
                .reviewDate(LocalDateTime.now())
                .verifiedPurchase(true)
                .likes(10)
                .build();

        Review review2 = Review.builder()
                .id("r2")
                .userId("user2")
                .userName("Jane Doe")
                .rating(4)
                .comment("Good product")
                .reviewDate(LocalDateTime.now())
                .verifiedPurchase(true)
                .likes(5)
                .build();

        Question question = Question.builder()
                .id("q1")
                .question("Is it available?")
                .answer("Yes")
                .userId("user3")
                .createdAt(LocalDateTime.now())
                .answeredAt(LocalDateTime.now())
                .answered(true)
                .build();

        return Product.builder()
                .id("1")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .brand("Test Brand")
                .category("Test Category")
                .stock(10)
                .images(Arrays.asList("image1.jpg", "image2.jpg"))
                .specifications(new HashMap<String, String>() {{
                    put("color", "Red");
                    put("size", "M");
                }})
                .sellerId("seller1")
                .reviews(Arrays.asList(review1, review2))
                .questions(Collections.singletonList(question))
                .build();
    }
}
