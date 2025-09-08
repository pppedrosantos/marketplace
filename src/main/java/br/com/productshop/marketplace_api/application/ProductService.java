package br.com.productshop.marketplace_api.application;


import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.mapper.ProductMapper;
import br.com.productshop.marketplace_api.domain.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Retorna todos os produtos cadastrados
     * @return Lista com todos os produtos
     */
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca produtos por categoria
     * @param category Categoria desejada
     * @return Lista de produtos da categoria especificada
     */
    public List<ProductResponse> findByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Encontra produtos similares baseado na categoria
     * @param productId ID do produto de referência
     * @param limit Número máximo de produtos similares a retornar
     * @return Lista de produtos da mesma categoria, excluindo o produto de referência
     */
    public List<ProductResponse> findSimilarProducts(String productId, int limit) {
        return productRepository.findSimilarProducts(productId, limit)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca outros produtos do mesmo vendedor
     * @param sellerId ID do vendedor
     * @param excludedProductId ID do produto a ser excluído da lista (opcional)
     * @return Lista de produtos do vendedor, exceto o produto excluído
     */
    public List<ProductResponse> findBySellerWithoutProduct(String sellerId, String excludedProductId) {
        return productRepository.findBySellerId(sellerId)
                .stream()
                .filter(p -> excludedProductId == null || !p.getId().equals(excludedProductId))
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca produtos aplicando múltiplos filtros
     * @param minPrice Preço mínimo (opcional)
     * @param maxPrice Preço máximo (opcional)
     * @param category Categoria específica (opcional)
     * @param minRating Avaliação mínima (opcional)
     * @return Lista de produtos que atendem aos critérios dos filtros
     */
    public List<ProductResponse> findWithFilters(Double minPrice, Double maxPrice,
                                       String category, Double minRating) {
        return productRepository.findWithFilters(minPrice, maxPrice, category, minRating)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }
}
