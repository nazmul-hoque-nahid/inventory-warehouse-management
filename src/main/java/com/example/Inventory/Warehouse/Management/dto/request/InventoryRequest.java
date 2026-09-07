package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.Product;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Setter
@Getter
public class InventoryRequest {
    @NotNull(message = "Warehouse is required")
    private Long warehouseId;
    @NotNull(message = "Product is required")
    private Long productId;
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}
