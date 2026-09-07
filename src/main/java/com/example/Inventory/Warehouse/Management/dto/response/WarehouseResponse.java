package com.example.Inventory.Warehouse.Management.dto.response;

import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseResponse {
    private Long id;
    private Long managerId;
    private String managerName;
    private String name;
    private String location;
    private Warehouse.Status status;
}
