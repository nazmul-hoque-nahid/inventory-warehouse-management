package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.StockTransferRequest;
import com.example.Inventory.Warehouse.Management.dto.response.StockTransferResponse;
import com.example.Inventory.Warehouse.Management.entity.StockTransfer;
import com.example.Inventory.Warehouse.Management.security.CustomUserDetails;
import com.example.Inventory.Warehouse.Management.service.StockTransferService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock-transfers")
@SecurityRequirement(name = "bearerAuth")
public class StockTransferController {
    private final StockTransferService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<StockTransferResponse>create(
            @Valid@RequestBody StockTransferRequest request,
            @AuthenticationPrincipal CustomUserDetails principal
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(principal.getId(),request));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_MANAGER')")
    public ResponseEntity<Page<StockTransferResponse>>getAll(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.getAll(page, size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_MANAGER')")
    public ResponseEntity<StockTransferResponse>findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<StockTransferResponse>complete(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails principle){
        return ResponseEntity.ok(service.complete(id, principle.getId()));
    }
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
    public ResponseEntity<StockTransferResponse>cancel(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails principle){
        return ResponseEntity.ok(service.cancel(id, principle.getId()));
    }

}
