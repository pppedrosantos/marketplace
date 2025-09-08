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
class FindBySellerUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FindBySellerUseCase findBySellerUseCase;

    @Test
    void shouldFindSellerProducts() {
        // Arrange
        String sellerId = "seller1";

        Product product1 = Product.builder()
                .id("1")
                .name("Product 1")
                .sellerId(sellerId)
                .price(BigDecimal.valueOf(99.99))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Product 2")
                .sellerId(sellerId)
                .price(BigDecimal.valueOf(149.99))
                .build();

        when(productRepository.findBySellerId(sellerId))
                .thenReturn(Arrays.asList(product1, product2));

        when(productMapper.toResponse(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .sellerId(p.getSellerId())
                    .price(p.getPrice())
                    .build();
        });

        // Act
        List<ProductResponse> result = findBySellerUseCase.execute(sellerId, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getSellerId().equals(sellerId)));
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(2)).toResponse(any());
    }

    @Test
    void shouldExcludeSpecifiedProduct() {
        // Arrange
        String sellerId = "seller1";
        String excludeProductId = "2";

        Product product1 = Product.builder()
                .id("1")
                .name("Product 1")
                .sellerId(sellerId)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Product 2")
                .sellerId(sellerId)
                .build();

        Product product3 = Product.builder()
                .id("3")
                .name("Product 3")
                .sellerId(sellerId)
                .build();

        when(productRepository.findBySellerId(sellerId))
                .thenReturn(Arrays.asList(product1, product2, product3));

        when(productMapper.toResponse(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .sellerId(p.getSellerId())
                    .build();
        });

        // Act
        List<ProductResponse> result = findBySellerUseCase.execute(sellerId, excludeProductId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.stream().anyMatch(p -> p.getId().equals(excludeProductId)));
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(3)).toResponse(any());
    }

    @Test
    void shouldReturnEmptyListWhenSellerHasNoProducts() {
        // Arrange
        String sellerId = "newSeller";
        when(productRepository.findBySellerId(sellerId))
                .thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findBySellerUseCase.execute(sellerId, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldHandleNullSellerId() {
        // Arrange
        when(productRepository.findBySellerId(null))
                .thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findBySellerUseCase.execute(null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findBySellerId(null);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllProductsWhenExcludeIdDoesNotExist() {
        // Arrange
        String sellerId = "seller1";
        String nonExistentProductId = "999";

        Product product1 = Product.builder()
                .id("1")
                .name("Product 1")
                .sellerId(sellerId)
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Product 2")
                .sellerId(sellerId)
                .build();

        when(productRepository.findBySellerId(sellerId))
                .thenReturn(Arrays.asList(product1, product2));

        when(productMapper.toResponse(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .sellerId(p.getSellerId())
                    .build();
        });

        // Act
        List<ProductResponse> result = findBySellerUseCase.execute(sellerId, nonExistentProductId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(2)).toResponse(any());
    }
}
