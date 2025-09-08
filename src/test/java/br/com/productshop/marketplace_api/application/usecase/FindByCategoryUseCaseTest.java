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
class FindByCategoryUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FindByCategoryUseCase findByCategoryUseCase;

    @Test
    void shouldReturnProductsWhenCategoryExists() {
        // Arrange
        String category = "Eletrônicos";
        Product product1 = Product.builder()
                .id("1")
                .name("Smartphone")
                .category(category)
                .price(BigDecimal.valueOf(999.99))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Tablet")
                .category(category)
                .price(BigDecimal.valueOf(1499.99))
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        ProductResponse response1 = ProductResponse.builder()
                .id("1")
                .name("Smartphone")
                .category(category)
                .price(BigDecimal.valueOf(999.99))
                .build();

        ProductResponse response2 = ProductResponse.builder()
                .id("2")
                .name("Tablet")
                .category(category)
                .price(BigDecimal.valueOf(1499.99))
                .build();

        when(productRepository.findByCategory(category)).thenReturn(products);
        when(productMapper.toResponse(product1)).thenReturn(response1);
        when(productMapper.toResponse(product2)).thenReturn(response2);

        // Act
        List<ProductResponse> result = findByCategoryUseCase.execute(category);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Smartphone", result.get(0).getName());
        assertEquals("Tablet", result.get(1).getName());
        verify(productRepository).findByCategory(category);
        verify(productMapper, times(2)).toResponse(any());
    }

    @Test
    void shouldReturnEmptyListWhenCategoryDoesNotExist() {
        // Arrange
        String category = "NonExistentCategory";
        when(productRepository.findByCategory(category)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findByCategoryUseCase.execute(category);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findByCategory(category);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldHandleNullCategory() {
        // Arrange
        when(productRepository.findByCategory(null)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = findByCategoryUseCase.execute(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findByCategory(null);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void shouldReturnAllProductsFromSameCategory() {
        // Arrange
        String category = "Vestuário";
        Product product1 = Product.builder()
                .id("1")
                .name("Camiseta")
                .category(category)
                .price(BigDecimal.valueOf(49.99))
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Calça")
                .category(category)
                .price(BigDecimal.valueOf(99.99))
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findByCategory(category)).thenReturn(products);
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
        List<ProductResponse> result = findByCategoryUseCase.execute(category);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals(category)));
        verify(productRepository).findByCategory(category);
        verify(productMapper, times(2)).toResponse(any());
    }
}
