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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductUseCaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FindProductUseCase findProductUseCase;

    @Test
    void shouldReturnProductWhenFound() {
        // Arrange
        String productId = "1";
        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .price(BigDecimal.TEN)
                .build();

        ProductResponse expectedResponse = ProductResponse.builder()
                .id(productId)
                .name("Test Product")
                .price(BigDecimal.TEN)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // Act
        ProductResponse response = findProductUseCase.execute(productId);

        // Assert
        assertNotNull(response);
        assertEquals(productId, response.getId());
        assertEquals("Test Product", response.getName());
        verify(productRepository).findById(productId);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        String productId = "nonexistent";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> findProductUseCase.execute(productId));
        verify(productRepository).findById(productId);
        verify(productMapper, never()).toResponse(any());
    }
}
