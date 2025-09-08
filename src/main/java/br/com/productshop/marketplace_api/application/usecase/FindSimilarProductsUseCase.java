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
public class FindSimilarProductsUseCase {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> execute(String productId, int limit) {
        return productRepository.findSimilarProducts(productId, limit)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
