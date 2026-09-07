package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PurchaseOrderRequest {
    @NotNull(message = "supplied id required")
    private Long supplierId;
    @NotNull(message = "warehouse id required")
    private Long warehouseId;
    @NotBlank(message = "items required")
    private List<PurchaseOrderItemRequest> items=new ArrayList<>();
}
