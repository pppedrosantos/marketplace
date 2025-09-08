package br.com.productshop.marketplace_api.application;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.exception.CategoryNotFoundException;
import br.com.productshop.marketplace_api.application.exception.InvalidFilterException;
import br.com.productshop.marketplace_api.application.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

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
    private ProductService productService;

    private ProductResponse smartphone;
    private ProductResponse tablet;
    private List<ProductResponse> products;

    @BeforeEach
    void setUp() {
        smartphone = ProductResponse.builder()
                .id("1")
                .name("iPhone")
                .price(BigDecimal.valueOf(1999.99))
                .category("Smartphones")
                .rating(4.5)
                .sellerId("seller1")
                .build();

        tablet = ProductResponse.builder()
                .id("2")
                .name("iPad")
                .price(BigDecimal.valueOf(2999.99))
                .category("Tablets")
                .rating(4.8)
                .sellerId("seller1")
                .build();

        products = Arrays.asList(smartphone, tablet);
    }

    @Nested
    @DisplayName("Find All Products")
    class FindAllProducts {
        @Test
        @DisplayName("Should return all products successfully")
        void shouldReturnAllProducts() {
            when(listProductsUseCase.execute()).thenReturn(products);

            List<ProductResponse> result = productService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("iPhone", result.get(0).getName());
            verify(listProductsUseCase).execute();
        }

        @Test
        @DisplayName("Should return empty list when no products exist")
        void shouldReturnEmptyList() {
            when(listProductsUseCase.execute()).thenReturn(Collections.emptyList());

            List<ProductResponse> result = productService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(listProductsUseCase).execute();
        }
    }

    @Nested
    @DisplayName("Find Product By ID")
    class FindProductById {
        @Test
        @DisplayName("Should return product when ID exists")
        void shouldReturnProduct() {
            when(findProductUseCase.execute("1")).thenReturn(smartphone);

            ProductResponse result = productService.findById("1");

            assertNotNull(result);
            assertEquals("1", result.getId());
            assertEquals("iPhone", result.getName());
            verify(findProductUseCase).execute("1");
        }

        @Test
        @DisplayName("Should throw exception when ID is empty")
        void shouldThrowExceptionWhenIdIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                () -> productService.findById(""));

            verify(findProductUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("Find Similar Products")
    class FindSimilarProducts {
        @Test
        @DisplayName("Should return similar products")
        void shouldReturnSimilarProducts() {
            when(findSimilarProductsUseCase.execute("1", 5)).thenReturn(Collections.singletonList(tablet));

            List<ProductResponse> result = productService.findSimilarProducts("1", 5);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("iPad", result.get(0).getName());
            verify(findSimilarProductsUseCase).execute("1", 5);
        }

        @Test
        @DisplayName("Should throw exception when limit is zero or negative")
        void shouldThrowExceptionWhenLimitIsInvalid() {
            assertThrows(InvalidFilterException.class,
                () -> productService.findSimilarProducts("1", 0));

            verify(findSimilarProductsUseCase, never()).execute(any(), anyInt());
        }

        @Test
        @DisplayName("Should throw exception when ID is empty")
        void shouldThrowExceptionWhenIdIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                () -> productService.findSimilarProducts("", 5));

            verify(findSimilarProductsUseCase, never()).execute(any(), anyInt());
        }
    }

    @Nested
    @DisplayName("Find By Category")
    class FindByCategory {
        @Test
        @DisplayName("Should return products by category")
        void shouldReturnProductsByCategory() {
            when(findByCategoryUseCase.execute("Smartphones"))
                .thenReturn(Collections.singletonList(smartphone));

            List<ProductResponse> result = productService.findByCategory("Smartphones");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("iPhone", result.get(0).getName());
            verify(findByCategoryUseCase).execute("Smartphones");
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void shouldThrowExceptionWhenCategoryNotFound() {
            when(findByCategoryUseCase.execute("InvalidCategory"))
                .thenReturn(Collections.emptyList());

            assertThrows(CategoryNotFoundException.class,
                () -> productService.findByCategory("InvalidCategory"));

            verify(findByCategoryUseCase).execute("InvalidCategory");
        }

        @Test
        @DisplayName("Should throw exception when category is empty")
        void shouldThrowExceptionWhenCategoryIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                () -> productService.findByCategory(""));

            verify(findByCategoryUseCase, never()).execute(any());
        }
    }

    @Nested
    @DisplayName("Find By Seller")
    class FindBySeller {
        @Test
        @DisplayName("Should return seller products")
        void shouldReturnSellerProducts() {
            when(findBySellerUseCase.execute("seller1", null)).thenReturn(products);

            List<ProductResponse> result = productService.findBySellerWithoutProduct("seller1", null);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(findBySellerUseCase).execute("seller1", null);
        }

        @Test
        @DisplayName("Should return filtered seller products when excluding a product")
        void shouldReturnFilteredSellerProducts() {
            when(findBySellerUseCase.execute("seller1", "1"))
                .thenReturn(Collections.singletonList(tablet));

            List<ProductResponse> result = productService.findBySellerWithoutProduct("seller1", "1");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("iPad", result.get(0).getName());
            verify(findBySellerUseCase).execute("seller1", "1");
        }

        @Test
        @DisplayName("Should throw exception when seller ID is empty")
        void shouldThrowExceptionWhenSellerIdIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                () -> productService.findBySellerWithoutProduct("", null));

            verify(findBySellerUseCase, never()).execute(any(), any());
        }
    }

    @Nested
    @DisplayName("Search With Filters")
    class SearchWithFilters {
        @Test
        @DisplayName("Should return filtered products")
        void shouldReturnFilteredProducts() {
            // Arrange
            Double minPrice = 1000.0;
            Double maxPrice = 3000.0;
            String category = "Smartphones";
            Double minRating = 4.0;

            when(findByCategoryUseCase.execute(category))
                .thenReturn(Collections.singletonList(smartphone));
            when(searchProductsUseCase.execute(minPrice, maxPrice, category, minRating))
                .thenReturn(Collections.singletonList(smartphone));

            // Act
            List<ProductResponse> result = productService.searchWithFilters(
                minPrice, maxPrice, category, minRating);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("iPhone", result.get(0).getName());
            verify(findByCategoryUseCase).execute(category);
            verify(searchProductsUseCase).execute(minPrice, maxPrice, category, minRating);
        }

        @Test
        @DisplayName("Should throw exception when min price is negative")
        void shouldThrowExceptionWhenMinPriceIsNegative() {
            assertThrows(InvalidFilterException.class,
                () -> productService.searchWithFilters(-1.0, 100.0, null, null));

            verify(searchProductsUseCase, never()).execute(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when max price is negative")
        void shouldThrowExceptionWhenMaxPriceIsNegative() {
            assertThrows(InvalidFilterException.class,
                () -> productService.searchWithFilters(0.0, -1.0, null, null));

            verify(searchProductsUseCase, never()).execute(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when min price is greater than max price")
        void shouldThrowExceptionWhenMinPriceIsGreaterThanMaxPrice() {
            assertThrows(InvalidFilterException.class,
                () -> productService.searchWithFilters(100.0, 50.0, null, null));

            verify(searchProductsUseCase, never()).execute(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when rating is invalid")
        void shouldThrowExceptionWhenRatingIsInvalid() {
            assertThrows(InvalidFilterException.class,
                () -> productService.searchWithFilters(null, null, null, 5.1));

            verify(searchProductsUseCase, never()).execute(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should throw exception when category not found")
        void shouldThrowExceptionWhenCategoryNotFound() {
            // Arrange
            String category = "InvalidCategory";
            when(findByCategoryUseCase.execute(category))
                .thenReturn(Collections.emptyList());

            // Act & Assert
            assertThrows(CategoryNotFoundException.class,
                () -> productService.searchWithFilters(null, null, category, null));

            verify(findByCategoryUseCase).execute(category);
            verify(searchProductsUseCase, never()).execute(any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should accept null filters")
        void shouldAcceptNullFilters() {
            when(searchProductsUseCase.execute(null, null, null, null))
                .thenReturn(products);

            List<ProductResponse> result = productService.searchWithFilters(
                null, null, null, null);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(findByCategoryUseCase, never()).execute(any());
            verify(searchProductsUseCase).execute(null, null, null, null);
        }
    }
}
