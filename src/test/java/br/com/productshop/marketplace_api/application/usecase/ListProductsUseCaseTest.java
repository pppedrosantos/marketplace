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
class ListProductsUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ListProductsUseCase listProductsUseCase;

    @Test
    void shouldReturnAllProducts() {
        // Arrange
        Product product1 = Product.builder()
                .id("1")
                .name("Smartphone")
                .price(BigDecimal.valueOf(999.99))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Tablet")
                .price(BigDecimal.valueOf(1499.99))
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return ProductResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .price(p.getPrice())
                    .build();
        });

        // Act
        List<ProductResponse> result = listProductsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Smartphone", result.get(0).getName());
        assertEquals("Tablet", result.get(1).getName());
        verify(productRepository).findAll();
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = listProductsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldMapAllProductFields() {
        // Arrange
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .brand("Test Brand")
                .category("Test Category")
                .stock(10)
                .rating(4.5)
                .build();

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(productMapper.toResponse(product)).thenReturn(
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .rating(product.getRating())
                        .build()
        );

        // Act
        List<ProductResponse> result = listProductsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        ProductResponse response = result.get(0);
        assertEquals("1", response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(BigDecimal.valueOf(99.99), response.getPrice());
        assertEquals("Test Brand", response.getBrand());
        assertEquals("Test Category", response.getCategory());
        assertEquals(10, response.getStock());
        assertEquals(4.5, response.getRating());
        verify(productRepository).findAll();
        verify(productMapper).toResponse(product);
    }
}
