package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTransferItemRequest {
    @NotNull(message = "product Id required")
    private Long productId;
    @NotNull(message = "Quantity required")
    @Min(value = 1,message = "minimum quantity is 1")
    private Integer quantity;
}
