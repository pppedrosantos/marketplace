package br.com.productshop.marketplace_api.domain.repository;

import br.com.productshop.marketplace_api.domain.entity.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<Product> findAll();
    Optional<Product> findById(String id);
    List<Product> findByCategory(String category);
    List<Product> findBySellerId(String sellerId);
    List<Product> findWithFilters(Double minPrice, Double maxPrice, String category, Double minRating);
    List<Product> findSimilarProducts(String productId, int limit);
}
