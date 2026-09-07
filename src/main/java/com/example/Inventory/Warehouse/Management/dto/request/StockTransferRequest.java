package com.example.Inventory.Warehouse.Management.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StockTransferRequest {
    @NotNull(message = "Source warehouse ID required")
    private Long sourceWarehouseId;
    @NotNull(message = "Destination warehouse ID required")
    private Long destinationWarehouseId;
    @Size(max =200,message = "note length maximum 200")
    private String note;
    @NotEmpty(message = "Items required")
    private List<StockTransferItemRequest>items=new ArrayList<>();
}
