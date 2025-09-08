package br.com.productshop.marketplace_api.application.mapper;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.domain.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .category(product.getCategory())
                .stock(product.getStock())
                .images(product.getImages())
                .specifications(product.getSpecifications())
                .sellerId(product.getSellerId())
                .rating(product.getRating())
                .build();
    }
}
