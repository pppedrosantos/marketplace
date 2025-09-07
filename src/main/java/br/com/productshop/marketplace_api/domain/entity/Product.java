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
    private String id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private String category;
    private Integer stock;
    private List<String> images;
    private Map<String, String> specifications;

}
