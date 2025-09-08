package br.com.productshop.marketplace_api.application.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private String category;
    private Integer stock;
    private List<String> images;
    private Map<String, String> specifications;
    private String sellerId;
    private Double rating;
}
