package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductUpdateRequest {
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;
    @Size(max = 1000,message = "maximum description size is 1000")
    private String description;
    @Positive(message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
    private Long categoryId;
}
