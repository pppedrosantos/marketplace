package br.com.productshop.marketplace_api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /** Identificador único do produto */
    private String id;

    /** Nome/título do produto */
    private String name;

    /** Descrição detalhada do produto */
    private String description;

    /** Preço atual do produto */
    private Double price;

    /** Marca do produto */
    private String brand;

    /** Categoria do produto (ex: Eletrônicos, Roupas, etc) */
    private String category;

    /** Quantidade disponível em estoque */
    private Integer stock;

    /** Lista de URLs das imagens do produto */
    private List<String> images;

    /** Especificações técnicas do produto em formato chave-valor */
    private Map<String, String> specifications;

    /** Identificador único do vendedor do produto */
    private String sellerId;

    /** Avaliação média do produto (0-5) */
    private Double rating;
}
