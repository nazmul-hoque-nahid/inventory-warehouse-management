package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.PurchaseOrderRequest;
import com.example.Inventory.Warehouse.Management.dto.response.PurchaseOrderResponse;
import com.example.Inventory.Warehouse.Management.dto.response.UserResponse;
import com.example.Inventory.Warehouse.Management.entity.PurchaseOrder;
import com.example.Inventory.Warehouse.Management.security.CustomUserDetails;
import com.example.Inventory.Warehouse.Management.service.PurchaseOrderService;
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
@RequestMapping("/api/purchase-orders")
@SecurityRequirement(name = "bearerAuth")
public class PurchaseOrderController {
      private final PurchaseOrderService service;
      @PostMapping
      @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER','INVENTORY_MANAGER')")
      public ResponseEntity<PurchaseOrderResponse>create(
              @Valid@RequestBody PurchaseOrderRequest request,
              @AuthenticationPrincipal CustomUserDetails principal
                                                         ){
            Long currentUserId = principal.getId();
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(currentUserId,request));
      }
      @GetMapping
      @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER','INVENTORY_MANAGER')")
      public ResponseEntity<Page<PurchaseOrderResponse>>getAllPurchases(
              @RequestParam(defaultValue = "0")int page,
              @RequestParam(defaultValue = "10")int size
      ){
         return ResponseEntity.ok(service.getAllPurchases(page, size));
      }
      @GetMapping("/{id}")
      @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER','INVENTORY_MANAGER')")
      public ResponseEntity<PurchaseOrderResponse>findById(@PathVariable Long id){
            return ResponseEntity.ok(service.findById(id));
      }
      @PatchMapping("/{id}/receive")
      @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER')")
      public ResponseEntity<PurchaseOrderResponse> receive(
              @PathVariable Long id,
              @AuthenticationPrincipal CustomUserDetails principal
      )
      {
            Long currentUserId = principal.getId();
            return ResponseEntity.ok(service.receive(currentUserId,id));
      }
      @PatchMapping("/{id}/cancel")
      @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE_MANAGER','INVENTORY_MANAGER')")
      public ResponseEntity<PurchaseOrderResponse> cancel(
              @PathVariable Long id,
              @AuthenticationPrincipal CustomUserDetails principal
      )
      {
            return ResponseEntity.ok(service.cancel(principal.getId(),id));
      }

}
