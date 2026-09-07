package com.example.Inventory.Warehouse.Management.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryResponse {
    private Long id;
    private Long warehouseId;
    private String warehouseName;
    private Long productId;
    private String productName;
    private Integer quantity;
}
