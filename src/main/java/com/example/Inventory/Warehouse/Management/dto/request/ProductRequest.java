package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductRequest {
    @NotBlank(message = "sku is required")
    private String sku;
    @NotBlank(message = "name is required")
    private String name;
    @Size(max = 1000,message = "maximum description size is 1000")
    private String description;
    @NotNull(message = "unit price is required")
    @Positive(message = "Unit price must be positive ")
    private BigDecimal unitPrice;
    @NotNull(message = "category id required")
    private Long categoryId;
}
