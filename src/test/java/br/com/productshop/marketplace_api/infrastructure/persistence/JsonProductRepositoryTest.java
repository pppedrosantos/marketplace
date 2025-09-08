package br.com.productshop.marketplace_api.infrastructure.persistence;

import br.com.productshop.marketplace_api.domain.entity.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JsonProductRepositoryTest {

    private JsonProductRepository repository;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() throws IOException {
        // Cria produtos de teste
        List<Product> testProducts = List.of(
            Product.builder()
                .id("1")
                .name("Smartphone")
                .description("Um smartphone incrível")
                .price(BigDecimal.valueOf(1999.99))
                .category("Eletrônicos")
                .sellerId("seller1")
                .rating(4.5)
                .build(),
            Product.builder()
                .id("2")
                .name("Tablet")
                .description("Um tablet poderoso")
                .price(BigDecimal.valueOf(2999.99))
                .category("Eletrônicos")
                .sellerId("seller1")
                .rating(4.8)
                .build(),
            Product.builder()
                .id("3")
                .name("Camiseta")
                .description("Uma camiseta confortável")
                .price(BigDecimal.valueOf(79.99))
                .category("Vestuário")
                .sellerId("seller2")
                .rating(4.0)
                .build()
        );

        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(testProducts);

        repository = new JsonProductRepository(mockedMapper);
    }

    @Test
    void shouldFindAllProducts() {
        // Act
        List<Product> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Smartphone", result.get(0).getName());
        assertEquals("Tablet", result.get(1).getName());
        assertEquals("Camiseta", result.get(2).getName());
    }

    @Test
    void shouldFindProductById() {
        // Act
        Optional<Product> result = repository.findById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Smartphone", result.get().getName());
        assertEquals("Eletrônicos", result.get().getCategory());
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        // Act
        Optional<Product> result = repository.findById("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindProductsByCategory() {
        // Act
        List<Product> result = repository.findByCategory("Eletrônicos");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals("Eletrônicos")));
    }

    @Test
    void shouldFindProductsBySellerId() {
        // Act
        List<Product> result = repository.findBySellerId("seller1");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getSellerId().equals("seller1")));
    }

    @Test
    void shouldFindProductsWithPriceFilter() {
        // Act
        List<Product> result = repository.findWithFilters(1000.0, 3000.0, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p ->
            p.getPrice().compareTo(BigDecimal.valueOf(1000.0)) >= 0 &&
            p.getPrice().compareTo(BigDecimal.valueOf(3000.0)) <= 0
        ));
    }

    @Test
    void shouldFindProductsWithCategoryFilter() {
        // Act
        List<Product> result = repository.findWithFilters(null, null, "Vestuário", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Vestuário", result.get(0).getCategory());
    }

    @Test
    void shouldFindProductsWithRatingFilter() {
        // Act
        List<Product> result = repository.findWithFilters(null, null, null, 4.5);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getRating() >= 4.5));
    }

    @Test
    void shouldFindSimilarProducts() {
        // Act
        List<Product> result = repository.findSimilarProducts("1", 5);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tablet", result.get(0).getName());
        assertEquals("Eletrônicos", result.get(0).getCategory());
        assertFalse(result.stream().anyMatch(p -> p.getId().equals("1")));
    }

    @Test
    void shouldLimitSimilarProducts() {
        // Act
        List<Product> result = repository.findSimilarProducts("1", 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoSimilarProducts() {
        // Act
        List<Product> result = repository.findSimilarProducts("3", 5);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleAllNullFilters() {
        // Act
        List<Product> result = repository.findWithFilters(null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
