package com.assignment_two_starter.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @NotNull(message = "Price must not be null")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    private Integer stockQuantity;

    private String description;

    public @NotBlank(message = "Product name must not be blank") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Product name must not be blank") String name) {
        this.name = name;
    }

    public @NotNull(message = "Price must not be null") @Min(value = 0, message = "Price must be greater than or equal to 0") Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Price must not be null") @Min(value = 0, message = "Price must be greater than or equal to 0") Double price) {
        this.price = price;
    }

    public @Min(value = 0, message = "Stock quantity must be greater than or equal to 0") Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(@Min(value = 0, message = "Stock quantity must be greater than or equal to 0") Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

