package br.com.productshop.marketplace_api.application.usecase;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.mapper.ProductMapper;
import br.com.productshop.marketplace_api.domain.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchProductsUseCase {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> execute(Double minPrice, Double maxPrice, String category, Double minRating) {
        return productRepository.findWithFilters(minPrice, maxPrice, category, minRating)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
