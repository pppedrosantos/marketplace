package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API para gerenciamento de produtos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductResource {
    private final ListProductsUseCase listProductsUseCase;
    private final FindProductUseCase findProductUseCase;
    private final FindSimilarProductsUseCase findSimilarProductsUseCase;
    private final FindByCategoryUseCase findByCategoryUseCase;
    private final FindBySellerUseCase findBySellerUseCase;
    private final SearchProductsUseCase searchProductsUseCase;

    @GetMapping
    @Operation(
        summary = "Listar todos os produtos",
        description = "Retorna uma lista com todos os produtos disponíveis"
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(listProductsUseCase.execute());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar produto por ID",
        description = "Retorna um produto específico com base no ID fornecido"
    )
    @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(findProductUseCase.execute(id));
    }

    @GetMapping("/{id}/similar")
    @Operation(
        summary = "Buscar produtos similares",
        description = "Retorna produtos similares baseados na mesma categoria do produto informado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos similares encontrada com sucesso")
    public ResponseEntity<List<ProductResponse>> getSimilarProducts(
            @PathVariable String id,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(findSimilarProductsUseCase.execute(id, limit));
    }

    @GetMapping("/category/{category}")
    @Operation(
        summary = "Buscar produtos por categoria",
        description = "Retorna todos os produtos de uma categoria específica"
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria encontrada com sucesso")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(findByCategoryUseCase.execute(category));
    }

    @GetMapping("/seller/{sellerId}/products")
    @Operation(
        summary = "Buscar produtos do vendedor",
        description = "Retorna todos os produtos de um vendedor específico"
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos do vendedor encontrada com sucesso")
    public ResponseEntity<List<ProductResponse>> getSellerProducts(
            @PathVariable String sellerId,
            @RequestParam(required = false) String excludeProductId) {
        return ResponseEntity.ok(findBySellerUseCase.execute(sellerId, excludeProductId));
    }

    @GetMapping("/search")
    @Operation(
        summary = "Buscar produtos com filtros",
        description = "Retorna produtos que atendem aos critérios de filtro especificados"
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos filtrada encontrada com sucesso")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minRating) {
        return ResponseEntity.ok(searchProductsUseCase.execute(minPrice, maxPrice, category, minRating));
    }
}
