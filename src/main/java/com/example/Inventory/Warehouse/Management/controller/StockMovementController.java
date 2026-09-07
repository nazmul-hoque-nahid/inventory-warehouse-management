package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.response.StockMovementResponse;
import com.example.Inventory.Warehouse.Management.service.StockMovementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
public class StockMovementController {
    private final StockMovementService service;
    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(service.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<StockMovementResponse>> getByWarehouse(
            @PathVariable Long warehouseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                service.getByWarehouse(warehouseId, page, size)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<StockMovementResponse>> getByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                service.getByProduct(productId, page, size)
        );
    }
}