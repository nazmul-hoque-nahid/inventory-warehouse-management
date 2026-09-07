package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.ProductRequest;
import com.example.Inventory.Warehouse.Management.dto.request.ProductUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.ProductResponse;
import com.example.Inventory.Warehouse.Management.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService service;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse>create(@Valid@RequestBody ProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ProductResponse>>getAllProducts(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.getAllProducts(page,size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProductResponse>findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ProductResponse>update(@PathVariable Long id, @Valid@RequestBody ProductUpdateRequest request){
        return ResponseEntity.ok(service.update(id,request));
    }
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ProductResponse>>search(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String sku,
            @RequestParam(required = false)Long categoryId,
            @RequestParam(required = false)BigDecimal minPrice,
            @RequestParam(required = false)BigDecimal maxPrice,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
    return ResponseEntity.ok(service.search(name, sku, categoryId, minPrice, maxPrice, page, size));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<Valid>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
