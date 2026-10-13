package com.example.demo.dto;

import com.example.demo.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String specs;

    private String imageUrl;

    private String status;

    public Product toEntity() {
        Product product = new Product();
        product.setName(this.name);
        product.setCategory(this.category);
        product.setPrice(this.price);
        product.setSpecs(this.specs);
        product.setImageUrl(this.imageUrl);
        product.setStatus(this.status != null ? 
            Product.ProductStatus.valueOf(this.status) : 
            Product.ProductStatus.AVAILABLE);
        return product;
    }
}
