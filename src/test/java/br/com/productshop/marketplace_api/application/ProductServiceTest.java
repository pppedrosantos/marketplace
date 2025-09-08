package br.com.productshop.marketplace_api.application;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldFindAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
            createProduct("1", "Product 1"),
            createProduct("2", "Product 2")
        );
        List<ProductResponse> expectedResponses = Arrays.asList(
            createProductResponse("1", "Product 1"),
            createProductResponse("2", "Product 2")
        );

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses.get(0).getName(), result.get(0).getName());
        verify(productRepository).findAll();
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldFindProductById() {
        // Arrange
        String productId = "1";
        Product product = createProduct(productId, "Test Product");
        ProductResponse expectedResponse = createProductResponse(productId, "Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        // Act
        ProductResponse result = productService.findById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(productId);
        verify(productMapper).toResponse(product);
    }

    @Test
    void shouldFindByCategory() {
        // Arrange
        String category = "Electronics";
        List<Product> products = Arrays.asList(
            createProduct("1", "Product 1"),
            createProduct("2", "Product 2")
        );

        when(productRepository.findByCategory(category)).thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findByCategory(category);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldFindBySellerId() {
        // Arrange
        String sellerId = "seller1";
        List<Product> products = Arrays.asList(
            createProduct("1", "Product 1"),
            createProduct("2", "Product 2")
        );

        when(productRepository.findBySellerId(sellerId)).thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findBySellerId(sellerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldFindWithFilters() {
        // Arrange
        Double minPrice = 100.0;
        Double maxPrice = 500.0;
        String category = "Electronics";
        Double minRating = 4.0;

        List<Product> products = Arrays.asList(
            createProduct("1", "Product 1"),
            createProduct("2", "Product 2")
        );

        when(productRepository.findWithFilters(minPrice, maxPrice, category, minRating))
            .thenReturn(products);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findWithFilters(minPrice, maxPrice, category, minRating);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findWithFilters(minPrice, maxPrice, category, minRating);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldHandleProductNotFound() {
        // Arrange
        String productId = "nonexistent";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productService.findById(productId));
        verify(productRepository).findById(productId);
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoryProducts() {
        // Arrange
        String category = "NonexistentCategory";
        when(productRepository.findByCategory(category)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = productService.findByCategory(category);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findByCategory(category);
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void shouldFindSimilarProducts() {
        // Arrange
        String productId = "1";
        int limit = 5;
        List<Product> similarProducts = Arrays.asList(
            createProduct("2", "Similar Product 1"),
            createProduct("3", "Similar Product 2")
        );

        when(productRepository.findSimilarProducts(productId, limit)).thenReturn(similarProducts);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findSimilarProducts(productId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Similar Product 1", result.get(0).getName());
        assertEquals("Similar Product 2", result.get(1).getName());
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoSimilarProducts() {
        // Arrange
        String productId = "1";
        int limit = 5;
        when(productRepository.findSimilarProducts(productId, limit)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = productService.findSimilarProducts(productId, limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findSimilarProducts(productId, limit);
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    @Test
    void shouldFindBySellerWithoutProduct() {
        // Arrange
        String sellerId = "seller1";
        String excludedProductId = "excluded1";
        List<Product> sellerProducts = Arrays.asList(
            createProduct("2", "Seller Product 1"),
            createProduct("3", "Seller Product 2")
        );

        when(productRepository.findBySellerId(sellerId)).thenReturn(sellerProducts);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findBySellerWithoutProduct(sellerId, excludedProductId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldFindBySellerWithoutProductWhenExcludedProductNotPresent() {
        // Arrange
        String sellerId = "seller1";
        String excludedProductId = null;
        List<Product> sellerProducts = Arrays.asList(
            createProduct("1", "Seller Product 1"),
            createProduct("2", "Seller Product 2")
        );

        when(productRepository.findBySellerId(sellerId)).thenReturn(sellerProducts);
        when(productMapper.toResponse(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            return createProductResponse(p.getId(), p.getName());
        });

        // Act
        List<ProductResponse> result = productService.findBySellerWithoutProduct(sellerId, excludedProductId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsFromSeller() {
        // Arrange
        String sellerId = "nonexistentSeller";
        String excludedProductId = "excluded1";
        when(productRepository.findBySellerId(sellerId)).thenReturn(Collections.emptyList());

        // Act
        List<ProductResponse> result = productService.findBySellerWithoutProduct(sellerId, excludedProductId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findBySellerId(sellerId);
        verify(productMapper, never()).toResponse(any(Product.class));
    }

    private Product createProduct(String id, String name) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(BigDecimal.TEN)
                .category("Test Category")
                .build();
    }

    private ProductResponse createProductResponse(String id, String name) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .price(BigDecimal.TEN)
                .category("Test Category")
                .build();
    }
}
