package br.com.productshop.marketplace_api.application;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.exception.CategoryNotFoundException;
import br.com.productshop.marketplace_api.application.exception.InvalidFilterException;
import br.com.productshop.marketplace_api.application.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("ID do produto não pode ser vazio");
        }
        return findProductUseCase.execute(id);
    }

    public List<ProductResponse> findSimilarProducts(String id, int limit) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("ID do produto não pode ser vazio");
        }
        if (limit <= 0) {
            throw new InvalidFilterException("Limite deve ser maior que zero");
        }
        return findSimilarProductsUseCase.execute(id, limit);
    }

    public List<ProductResponse> findByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            throw new IllegalArgumentException("Categoria não pode ser vazia");
        }
        List<ProductResponse> products = findByCategoryUseCase.execute(category);
        if (products.isEmpty()) {
            throw new CategoryNotFoundException(category);
        }
        return products;
    }

    public List<ProductResponse> findBySellerWithoutProduct(String sellerId, String excludeProductId) {
        if (!StringUtils.hasText(sellerId)) {
            throw new IllegalArgumentException("ID do vendedor não pode ser vazio");
        }
        return findBySellerUseCase.execute(sellerId, excludeProductId);
    }

    public List<ProductResponse> searchWithFilters(Double minPrice, Double maxPrice, String category, Double minRating) {
        validatePriceFilter(minPrice, maxPrice);
        validateRatingFilter(minRating);
        validateCategory(category);
        return searchProductsUseCase.execute(minPrice, maxPrice, category, minRating);
    }

    private void validateCategory(String category) {
        if (StringUtils.hasText(category)) {
            List<ProductResponse> products = findByCategoryUseCase.execute(category);
            if (products.isEmpty()) {
                throw new CategoryNotFoundException(category);
            }
        }
    }

    private void validatePriceFilter(Double minPrice, Double maxPrice) {
        if (Objects.nonNull(minPrice) && minPrice < 0) {
            throw new InvalidFilterException("Preço mínimo não pode ser negativo");
        }
        if (Objects.nonNull(maxPrice) && maxPrice < 0) {
            throw new InvalidFilterException("Preço máximo não pode ser negativo");
        }
        if (Objects.nonNull(minPrice) && Objects.nonNull(maxPrice) && minPrice > maxPrice) {
            throw new InvalidFilterException("Preço mínimo não pode ser maior que o preço máximo");
        }
    }

    private void validateRatingFilter(Double minRating) {
        if (Objects.nonNull(minRating)) {
            if (minRating < 0 || minRating > 5) {
                throw new InvalidFilterException("Avaliação deve estar entre 0 e 5");
            }
        }
    }
}
