package br.com.productshop.marketplace_api.application;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ListProductsUseCase listProductsUseCase;
    private final FindProductUseCase findProductUseCase;
    private final FindSimilarProductsUseCase findSimilarProductsUseCase;
    private final FindByCategoryUseCase findByCategoryUseCase;
    private final FindBySellerUseCase findBySellerUseCase;
    private final SearchProductsUseCase searchProductsUseCase;

    public List<ProductResponse> findAll() {
        return listProductsUseCase.execute();
    }

    public ProductResponse findById(String id) {
        return findProductUseCase.execute(id);
    }

    public List<ProductResponse> findSimilarProducts(String id, int limit) {
        return findSimilarProductsUseCase.execute(id, limit);
    }

    public List<ProductResponse> findByCategory(String category) {
        return findByCategoryUseCase.execute(category);
    }

    public List<ProductResponse> findBySellerWithoutProduct(String sellerId, String excludeProductId) {
        return findBySellerUseCase.execute(sellerId, excludeProductId);
    }

    public List<ProductResponse> searchWithFilters(Double minPrice, Double maxPrice, String category, Double minRating) {
        return searchProductsUseCase.execute(minPrice, maxPrice, category, minRating);
    }
}
