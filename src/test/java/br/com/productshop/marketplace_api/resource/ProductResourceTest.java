package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.usecase.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {

    @Mock
    private ListProductsUseCase listProductsUseCase;

    @Mock
    private FindProductUseCase findProductUseCase;

    @Mock
    private FindSimilarProductsUseCase findSimilarProductsUseCase;

    @Mock
    private FindByCategoryUseCase findByCategoryUseCase;

    @Mock
    private FindBySellerUseCase findBySellerUseCase;

    @Mock
    private SearchProductsUseCase searchProductsUseCase;

    @InjectMocks
    private ProductResource productResource;

    @Test
    void shouldReturnAllProducts() {
        // Arrange
        List<ProductResponse> products = Arrays.asList(
            createProductResponse("1", "Product 1"),
            createProductResponse("2", "Product 2")
        );
        when(listProductsUseCase.execute()).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(listProductsUseCase).execute();
    }

    @Test
    void shouldReturnProductById() {
        // Arrange
        String productId = "1";
        ProductResponse product = createProductResponse(productId, "Test Product");
        when(findProductUseCase.execute(productId)).thenReturn(product);

        // Act
        ResponseEntity<ProductResponse> response = productResource.getProductById(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        verify(findProductUseCase).execute(productId);
    }

    @Test
    void shouldReturnSimilarProducts() {
        // Arrange
        String productId = "1";
        int limit = 5;
        List<ProductResponse> similarProducts = Arrays.asList(
            createProductResponse("2", "Similar 1"),
            createProductResponse("3", "Similar 2")
        );
        when(findSimilarProductsUseCase.execute(productId, limit)).thenReturn(similarProducts);

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.getSimilarProducts(productId, limit);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(findSimilarProductsUseCase).execute(productId, limit);
    }

    @Test
    void shouldReturnProductsByCategory() {
        // Arrange
        String category = "Electronics";
        List<ProductResponse> products = Arrays.asList(
            createProductResponse("1", "Product 1"),
            createProductResponse("2", "Product 2")
        );
        when(findByCategoryUseCase.execute(category)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.getProductsByCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(findByCategoryUseCase).execute(category);
    }

    @Test
    void shouldReturnSellerProducts() {
        // Arrange
        String sellerId = "seller1";
        String excludeProductId = "exclude1";
        List<ProductResponse> products = Arrays.asList(
            createProductResponse("1", "Product 1"),
            createProductResponse("2", "Product 2")
        );
        when(findBySellerUseCase.execute(sellerId, excludeProductId)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.getSellerProducts(sellerId, excludeProductId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(findBySellerUseCase).execute(sellerId, excludeProductId);
    }

    @Test
    void shouldSearchProducts() {
        // Arrange
        Double minPrice = 100.0;
        Double maxPrice = 500.0;
        String category = "Electronics";
        Double minRating = 4.0;
        List<ProductResponse> products = Arrays.asList(
            createProductResponse("1", "Product 1"),
            createProductResponse("2", "Product 2")
        );
        when(searchProductsUseCase.execute(minPrice, maxPrice, category, minRating)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.searchProducts(minPrice, maxPrice, category, minRating);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(searchProductsUseCase).execute(minPrice, maxPrice, category, minRating);
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(listProductsUseCase.execute()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<ProductResponse>> response = productResource.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(listProductsUseCase).execute();
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
