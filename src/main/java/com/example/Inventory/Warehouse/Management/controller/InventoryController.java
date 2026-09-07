package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.InventoryRequest;
import com.example.Inventory.Warehouse.Management.dto.response.InventoryResponse;
import com.example.Inventory.Warehouse.Management.service.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_MANAGER')")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {
    private final InventoryService service;

    @GetMapping
    public ResponseEntity<Page<InventoryResponse>>getAllInventories(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok(service.getAllInventories(page, size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse>findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryResponse>>search(
            @RequestParam(required = false)Long warehouseId,
            @RequestParam(required = false)Long productId,
            @RequestParam(required = false)Integer quantity,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
      return   ResponseEntity.ok(service.search(warehouseId, productId, quantity, page, size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
