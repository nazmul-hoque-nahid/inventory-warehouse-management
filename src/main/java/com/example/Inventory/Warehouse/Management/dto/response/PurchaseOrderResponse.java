package com.example.Inventory.Warehouse.Management.dto.response;

import com.example.Inventory.Warehouse.Management.entity.PurchaseOrder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class PurchaseOrderResponse {
    Long id;
    Long supplierId;
    String supplierName;
    Long warehouseId;
    String warehouseName;
    Long orderedById;
    String orderedByName;
    LocalDateTime orderDate;
    PurchaseOrder.OrderStatus status;
    List<PurchaseOrderItemResponse> items=new ArrayList<>();
    BigDecimal totalAmount;
}
