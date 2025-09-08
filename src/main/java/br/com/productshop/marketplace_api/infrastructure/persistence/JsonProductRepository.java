package br.com.productshop.marketplace_api.infrastructure.persistence;

import br.com.productshop.marketplace_api.domain.entity.Product;
import br.com.productshop.marketplace_api.domain.repository.IProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class JsonProductRepository implements IProductRepository {
    private final List<Product> products;

    public JsonProductRepository(ObjectMapper objectMapper) {
        this.products = loadProducts(objectMapper);
    }

    private List<Product> loadProducts(ObjectMapper objectMapper) {
        try {
            ClassPathResource resource = new ClassPathResource("products.json");
            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            log.error("Erro ao carregar produtos: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> findByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findBySellerId(String sellerId) {
        return products.stream()
                .filter(p -> p.getSellerId().equals(sellerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findWithFilters(Double minPrice, Double maxPrice, String category, Double minRating) {
        return products.stream()
                .filter(p -> minPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0)
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> minRating == null || p.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findSimilarProducts(String productId, int limit) {
        String category = findById(productId)
                .map(Product::getCategory)
                .orElse("");

        return products.stream()
                .filter(p -> p.getCategory().equals(category))
                .filter(p -> !p.getId().equals(productId))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
