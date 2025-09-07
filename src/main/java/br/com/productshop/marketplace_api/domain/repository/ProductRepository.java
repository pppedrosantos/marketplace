package br.com.productshop.marketplace_api.domain.repository;

import br.com.productshop.marketplace_api.domain.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    List<Product> findAll();
}
