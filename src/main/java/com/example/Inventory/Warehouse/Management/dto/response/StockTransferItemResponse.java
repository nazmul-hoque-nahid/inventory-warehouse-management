package com.example.Inventory.Warehouse.Management.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTransferItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
}
