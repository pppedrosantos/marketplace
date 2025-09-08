package br.com.productshop.marketplace_api.application;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private ProductResponse product1;
    private ProductResponse product2;
    private List<ProductResponse> productList;

    @BeforeEach
    void setUp() {
        product1 = ProductResponse.builder()
                .id("1")
                .name("Product 1")
                .build();
        product2 = ProductResponse.builder()
                .id("2")
                .name("Product 2")
                .build();
        productList = Arrays.asList(product1, product2);
    }

    @Test
    void shouldFindAllProducts() {
        // Arrange
        when(listProductsUseCase.execute()).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listProductsUseCase).execute();
    }

    @Test
    void shouldFindProductById() {
        // Arrange
        String id = "1";
        when(findProductUseCase.execute(id)).thenReturn(product1);

        // Act
        ProductResponse result = productService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(findProductUseCase).execute(id);
    }

    @Test
    void shouldFindSimilarProducts() {
        // Arrange
        String id = "1";
        int limit = 5;
        when(findSimilarProductsUseCase.execute(id, limit)).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.findSimilarProducts(id, limit);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(findSimilarProductsUseCase).execute(id, limit);
    }

    @Test
    void shouldFindByCategory() {
        // Arrange
        String category = "Electronics";
        when(findByCategoryUseCase.execute(category)).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.findByCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(findByCategoryUseCase).execute(category);
    }

    @Test
    void shouldFindBySellerWithoutProduct() {
        // Arrange
        String sellerId = "seller1";
        String excludeProductId = "exclude1";
        when(findBySellerUseCase.execute(sellerId, excludeProductId)).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.findBySellerWithoutProduct(sellerId, excludeProductId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(findBySellerUseCase).execute(sellerId, excludeProductId);
    }

    @Test
    void shouldSearchWithFilters() {
        // Arrange
        Double minPrice = 10.0;
        Double maxPrice = 100.0;
        String category = "Electronics";
        Double minRating = 4.0;
        when(searchProductsUseCase.execute(minPrice, maxPrice, category, minRating)).thenReturn(productList);

        // Act
        List<ProductResponse> result = productService.searchWithFilters(minPrice, maxPrice, category, minRating);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(searchProductsUseCase).execute(minPrice, maxPrice, category, minRating);
    }
}
