package br.com.productshop.marketplace_api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private String category;
    private Integer stock;
    private List<String> images;
    private Map<String, String> specifications;
    private String sellerId;
    private Double rating;
    private List<Question> questions;
}
