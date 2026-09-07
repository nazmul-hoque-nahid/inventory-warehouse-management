package com.example.Inventory.Warehouse.Management.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private Long categoryId;
    private String categoryName;
}
