package br.com.productshop.marketplace_api.application.usecase;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.mapper.ProductMapper;
import br.com.productshop.marketplace_api.domain.entity.Product;
import br.com.productshop.marketplace_api.domain.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchProductsUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private SearchProductsUseCase searchProductsUseCase;

    @Test
    void shouldFindProductsWithPriceRangeFilter() {
        // Arrange
        Double minPrice = 1000.0;
        Double maxPrice = 2000.0;

        Product product = Product.builder()
                .id("1")
                .name("Produto Test")
                .price(BigDecimal.valueOf(1500.0))
                .build();

        when(productRepository.findWithFilters(minPrice, maxPrice, null, null))
                .thenReturn(Collections.singletonList(product));
        when(productMapper.toResponse(product))
                .thenReturn(ProductResponse.builder()
                        .id("1")
                        .name("Produto Test")
                        .price(BigDecimal.valueOf(1500.0))
                        .build());

        // Act
        List<ProductResponse> result = searchProductsUseCase.execute(minPrice, maxPrice, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(1500.0), result.get(0).getPrice());
        verify(productRepository).findWithFilters(minPrice, maxPrice, null, null);
        verify(productMapper).toResponse(any());
    }

    @Test
    void shouldFindProductsWithCategoryAndMinRating() {
        // Arrange
        String category = "Eletrônicos";
        Double minRating = 4.0;

        Product product1 = Product.builder()
                .id("1")
                .name("Produto 1")
                .category(category)
                .rating(4.5)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Produto 2")
                .category(category)
                .rating(4.8)
                .build();

        when(productRepository.findWithFilters(null, null, category, minRating))
                .thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toResponse(any()))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    return ProductResponse.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .category(p.getCategory())
                            .rating(p.getRating())
                            .build();
                });

        // Act
        List<ProductResponse> result = searchProductsUseCase.execute(null, null, category, minRating);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getRating() >= minRating));
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals(category)));
        verify(productRepository).findWithFilters(null, null, category, minRating);
        verify(productMapper, times(2)).toResponse(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsMatchFilters() {
        // Arrange
        Double minPrice = 5000.0;
        String category = "Categoria Inexistente";

        when(productRepository.findWithFilters(minPrice, null, category, null))
                .thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = searchProductsUseCase.execute(minPrice, null, category, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findWithFilters(minPrice, null, category, null);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldHandleAllFiltersNull() {
        // Arrange
        when(productRepository.findWithFilters(null, null, null, null))
                .thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = searchProductsUseCase.execute(null, null, null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findWithFilters(null, null, null, null);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldCombineAllFilters() {
        // Arrange
        Double minPrice = 1000.0;
        Double maxPrice = 5000.0;
        String category = "Eletrônicos";
        Double minRating = 4.5;

        Product product = Product.builder()
                .id("1")
                .name("Produto Premium")
                .price(BigDecimal.valueOf(2999.99))
                .category(category)
                .rating(4.8)
                .build();

        when(productRepository.findWithFilters(minPrice, maxPrice, category, minRating))
                .thenReturn(Collections.singletonList(product));
        when(productMapper.toResponse(product))
                .thenReturn(ProductResponse.builder()
                        .id("1")
                        .name("Produto Premium")
                        .price(BigDecimal.valueOf(2999.99))
                        .category(category)
                        .rating(4.8)
                        .build());

        // Act
        List<ProductResponse> result = searchProductsUseCase.execute(minPrice, maxPrice, category, minRating);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ProductResponse response = result.get(0);
        assertTrue(response.getPrice().doubleValue() >= minPrice);
        assertTrue(response.getPrice().doubleValue() <= maxPrice);
        assertEquals(category, response.getCategory());
        assertTrue(response.getRating() >= minRating);
        verify(productRepository).findWithFilters(minPrice, maxPrice, category, minRating);
        verify(productMapper).toResponse(any());
    }
}
