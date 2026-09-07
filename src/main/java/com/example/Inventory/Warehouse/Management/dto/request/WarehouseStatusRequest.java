package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import jakarta.validation.constraints.NotNull;

public class WarehouseStatusRequest {

    @NotNull(message = "Status is required")
    private Warehouse.Status status;

    public Warehouse.Status getStatus() {
        return status;
    }

    public void setStatus(Warehouse.Status status) {
        this.status = status;
    }
}