package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private String imageUrl;
    private String specs;
    private String status;

    public static ProductResponse fromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setSpecs(product.getSpecs());
        response.setStatus(product.getStatus().name());
        return response;
    }

    public static ProductResponse summaryFromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}
