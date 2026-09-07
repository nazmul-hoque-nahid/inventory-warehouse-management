package com.example.Inventory.Warehouse.Management.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseOrderItemRequest {
    private   Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
