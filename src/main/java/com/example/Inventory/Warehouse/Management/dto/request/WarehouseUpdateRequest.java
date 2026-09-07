package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseUpdateRequest {
    @Size(min = 5,max = 300,message = "minimum size 5 and maximum 300")
    private String name;
    @Size(min = 5,max = 300,message = "minimum size 5 and maximum 300")
    private String location;
}