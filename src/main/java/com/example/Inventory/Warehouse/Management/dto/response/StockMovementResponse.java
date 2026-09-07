package com.example.Inventory.Warehouse.Management.dto.response;

import com.example.Inventory.Warehouse.Management.entity.StockMovement;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
public class StockMovementResponse {
    private Long id;
    private Long warehouseId;
    private String warehouseName;
    private Long productId;
    private String productName;
    private Long performedById;
    private String performedByName;
    private StockMovement.MovementType movementType;
    private Integer quantity;
    private Long stockTransferId;
    private LocalDateTime movementDate;
    private String note;
}