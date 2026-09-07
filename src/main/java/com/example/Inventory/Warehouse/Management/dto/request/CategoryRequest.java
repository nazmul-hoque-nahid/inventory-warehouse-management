package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank(message = "Category name required")
    private String name;
    @Size(max = 1000,message = "Maximum description size 1000 characters")
    private String description;
}
