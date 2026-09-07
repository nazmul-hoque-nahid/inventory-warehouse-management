package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.WarehouseRequest;
import com.example.Inventory.Warehouse.Management.dto.request.WarehouseStatusRequest;
import com.example.Inventory.Warehouse.Management.dto.request.WarehouseUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.WarehouseResponse;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import com.example.Inventory.Warehouse.Management.service.WarehouseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouses")
@SecurityRequirement(name = "bearerAuth")

@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService service;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<WarehouseResponse> create(@Valid@RequestBody WarehouseRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<Page<WarehouseResponse>>getAllWarehouses(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.findAllWarehouses(page, size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER','WAREHOUSE_STAFF')")
    public ResponseEntity<WarehouseResponse>findById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<WarehouseResponse> update(@PathVariable Long id, @RequestBody WarehouseUpdateRequest request){
        return ResponseEntity.ok(service.update(id,request));
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<WarehouseResponse>updateStatus(@PathVariable Long id, @Valid@RequestBody WarehouseStatusRequest request){
        return ResponseEntity.ok(service.updateStatus(id,request));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<Valid>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
