package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WarehouseRequest {
    @NotNull(message = "manager required")
    private Long managerId;
    @NotBlank(message = "warehouse name required")
    @Size(min = 5,max = 300,message = "minimum size 5 and maximum 300")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$",message = "Only English letters and Numbers support")
    private String name;
    @Size(min = 5,max = 300,message = "minimum size 5 and maximum 300")
    private String location;
}
