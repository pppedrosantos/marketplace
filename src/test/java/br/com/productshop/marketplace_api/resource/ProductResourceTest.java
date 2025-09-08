package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.ProductService;
import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("Product Resource Tests")
class ProductResourceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductResource productResource;

    private ProductResponse product1;
    private ProductResponse product2;
    private List<ProductResponse> products;

    @BeforeEach
    void setUp() {
        product1 = createProduct("1", "iPhone", 1999.99, "Smartphones", 4.5);
        product2 = createProduct("2", "Galaxy", 1799.99, "Smartphones", 4.3);
        products = Arrays.asList(product1, product2);
    }

    @Nested
    @DisplayName("GET /products")
    class GetAllProducts {
        @Test
        @DisplayName("Should return all products successfully")
        void shouldReturnAllProducts() {
            when(productService.findAll()).thenReturn(products);

            ResponseEntity<List<ProductResponse>> response = productResource.getAllProducts();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(productService).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void shouldReturnEmptyList() {
            when(productService.findAll()).thenReturn(Collections.emptyList());

            ResponseEntity<List<ProductResponse>> response = productResource.getAllProducts();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(productService).findAll();
        }
    }

    @Nested
    @DisplayName("GET /products/{id}")
    class GetProductById {
        @Test
        @DisplayName("Should return product when it exists")
        void shouldReturnProduct() {
            when(productService.findById("1")).thenReturn(product1);

            ResponseEntity<ProductResponse> response = productResource.getProductById("1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("1", response.getBody().getId());
            assertEquals("iPhone", response.getBody().getName());
            verify(productService).findById("1");
        }

        @Test
        @DisplayName("Should return 404 when product doesn't exist")
        void shouldReturn404WhenProductNotFound() {
            when(productService.findById("999")).thenThrow(new ProductNotFoundException("999"));

            ResponseEntity<ProductResponse> response = productResource.getProductById("999");

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
            verify(productService).findById("999");
        }
    }

    @Nested
    @DisplayName("GET /products/{id}/similar")
    class GetSimilarProducts {
        @Test
        @DisplayName("Should return similar products")
        void shouldReturnSimilarProducts() {
            when(productService.findSimilarProducts("1", 5)).thenReturn(Collections.singletonList(product2));

            ResponseEntity<List<ProductResponse>> response = productResource.getSimilarProducts("1", 5);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Galaxy", response.getBody().get(0).getName());
            verify(productService).findSimilarProducts("1", 5);
        }

        @Test
        @DisplayName("Should return empty list when no similar products exist")
        void shouldReturnEmptyListWhenNoSimilarProducts() {
            when(productService.findSimilarProducts("1", 5)).thenReturn(Collections.emptyList());

            ResponseEntity<List<ProductResponse>> response = productResource.getSimilarProducts("1", 5);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(productService).findSimilarProducts("1", 5);
        }
    }

    @Nested
    @DisplayName("GET /products/category/{category}")
    class GetProductsByCategory {
        @Test
        @DisplayName("Should return products by category")
        void shouldReturnProductsByCategory() {
            when(productService.findByCategory("Smartphones")).thenReturn(products);

            ResponseEntity<List<ProductResponse>> response = productResource.getProductsByCategory("Smartphones");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(productService).findByCategory("Smartphones");
        }

        @Test
        @DisplayName("Should return empty list when category has no products")
        void shouldReturnEmptyListWhenCategoryHasNoProducts() {
            when(productService.findByCategory("InvalidCategory")).thenReturn(Collections.emptyList());

            ResponseEntity<List<ProductResponse>> response = productResource.getProductsByCategory("InvalidCategory");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(productService).findByCategory("InvalidCategory");
        }
    }

    @Nested
    @DisplayName("GET /products/seller/{sellerId}/products")
    class GetSellerProducts {
        @Test
        @DisplayName("Should return seller products")
        void shouldReturnSellerProducts() {
            when(productService.findBySellerWithoutProduct("seller1", null)).thenReturn(products);

            ResponseEntity<List<ProductResponse>> response = productResource.getSellerProducts("seller1", null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(productService).findBySellerWithoutProduct("seller1", null);
        }

        @Test
        @DisplayName("Should return seller products excluding specific product")
        void shouldReturnSellerProductsExcludingProduct() {
            when(productService.findBySellerWithoutProduct("seller1", "1"))
                .thenReturn(Collections.singletonList(product2));

            ResponseEntity<List<ProductResponse>> response = productResource.getSellerProducts("seller1", "1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals("Galaxy", response.getBody().get(0).getName());
            verify(productService).findBySellerWithoutProduct("seller1", "1");
        }
    }

    @Nested
    @DisplayName("GET /products/search")
    class SearchProducts {
        @Test
        @DisplayName("Should return filtered products")
        void shouldReturnFilteredProducts() {
            Double minPrice = 1000.0;
            Double maxPrice = 2000.0;
            String category = "Smartphones";
            Double minRating = 4.0;

            when(productService.searchWithFilters(minPrice, maxPrice, category, minRating))
                .thenReturn(products);

            ResponseEntity<List<ProductResponse>> response = productResource.searchProducts(
                minPrice, maxPrice, category, minRating);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(productService).searchWithFilters(minPrice, maxPrice, category, minRating);
        }

        @Test
        @DisplayName("Should handle null filters")
        void shouldHandleNullFilters() {
            when(productService.searchWithFilters(null, null, null, null))
                .thenReturn(products);

            ResponseEntity<List<ProductResponse>> response = productResource.searchProducts(
                null, null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            verify(productService).searchWithFilters(null, null, null, null);
        }
    }

    private ProductResponse createProduct(String id, String name, double price, String category, double rating) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .price(BigDecimal.valueOf(price))
                .category(category)
                .rating(rating)
                .build();
    }
}
