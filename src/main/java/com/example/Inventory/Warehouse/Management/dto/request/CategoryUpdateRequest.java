package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
    @Size(min = 4,max = 500,message = "min 4 and max500")
    private String name;
    @Size(max = 1000,message = "Maximum description size 1000 characters")
    private String description;
}
