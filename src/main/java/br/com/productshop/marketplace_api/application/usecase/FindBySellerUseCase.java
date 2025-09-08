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
public class FindBySellerUseCase {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> execute(String sellerId, String excludeProductId) {
        List<ProductResponse> products = productRepository.findBySellerId(sellerId)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        if (excludeProductId != null) {
            return products.stream()
                    .filter(p -> !p.getId().equals(excludeProductId))
                    .collect(Collectors.toList());
        }

        return products;
    }
}
