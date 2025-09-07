package br.com.productshop.marketplace_api.application;


import br.com.productshop.marketplace_api.application.exception.ProductNotFoundException;
import br.com.productshop.marketplace_api.domain.entity.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ObjectMapper objectMapper;
    private List<Product> products;

    public ProductService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadProducts();
    }

    private void loadProducts() {
        try {
            ClassPathResource resource = new ClassPathResource("products.json");
            products = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            products = new ArrayList<>();
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    public Product findById(String id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findAll() {
        return products;
    }
}
