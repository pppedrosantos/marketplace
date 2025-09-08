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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindSimilarProductsUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FindSimilarProductsUseCase findSimilarProductsUseCase;

    @Test
    void shouldFindSimilarProductsWithLimit() {
        // Arrange
        String productId = "1";
        int limit = 2;

        Product similar1 = Product.builder()
                .id("2")
                .name("Similar Product 1")
                .category("Eletrônicos")
                .price(BigDecimal.valueOf(999.99))
                .build();

        Product similar2 = Product.builder()
                .id("3")
                .name("Similar Product 2")
                .category("Eletrônicos")
                .price(BigDecimal.valueOf(1099.99))
                .build();

        List<Product> similarProducts = Arrays.asList(similar1, similar2);

        when(productRepository.findSimilarProducts(productId, limit)).thenReturn(similarProducts);
        when(productMapper.toResponse(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .category(p.getCategory())
                    .price(p.getPrice())
                    .build();
        });

        // Act
        List<ProductResponse> result = findSimilarProductsUseCase.execute(productId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Similar Product 1", result.get(0).getName());
        assertEquals("Similar Product 2", result.get(1).getName());
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, times(2)).toResponse(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoSimilarProducts() {
        // Arrange
        String productId = "1";
        int limit = 5;
        when(productRepository.findSimilarProducts(productId, limit)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findSimilarProductsUseCase.execute(productId, limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldHandleZeroLimit() {
        // Arrange
        String productId = "1";
        int limit = 0;
        when(productRepository.findSimilarProducts(productId, limit)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findSimilarProductsUseCase.execute(productId, limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldRetainSameCategoryForSimilarProducts() {
        // Arrange
        String productId = "1";
        int limit = 3;
        String category = "Eletrônicos";

        Product similar1 = Product.builder()
                .id("2")
                .name("Similar 1")
                .category(category)
                .build();

        Product similar2 = Product.builder()
                .id("3")
                .name("Similar 2")
                .category(category)
                .build();

        when(productRepository.findSimilarProducts(productId, limit))
                .thenReturn(Arrays.asList(similar1, similar2));

        when(productMapper.toResponse(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .category(p.getCategory())
                    .build();
        });

        // Act
        List<ProductResponse> result = findSimilarProductsUseCase.execute(productId, limit);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals(category)));
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, times(2)).toResponse(any());
    }

    @Test
    void shouldNotIncludeOriginalProduct() {
        // Arrange
        String productId = "1";
        int limit = 2;

        Product similar1 = Product.builder()
                .id("2")
                .name("Similar 1")
                .build();

        when(productRepository.findSimilarProducts(productId, limit))
                .thenReturn(Collections.singletonList(similar1));

        when(productMapper.toResponse(similar1)).thenReturn(
                ProductResponse.builder()
                        .id("2")
                        .name("Similar 1")
                        .build()
        );

        // Act
        List<ProductResponse> result = findSimilarProductsUseCase.execute(productId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.stream().anyMatch(p -> p.getId().equals(productId)));
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper).toResponse(any());
    }
}
