package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.SupplierRequest;
import com.example.Inventory.Warehouse.Management.dto.request.SupplierUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.SupplierResponse;
import com.example.Inventory.Warehouse.Management.service.SupplierService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
public class SupplierController {
    private final SupplierService service;
    @PostMapping
    public ResponseEntity<SupplierResponse>create(@Valid@RequestBody SupplierRequest request){
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @GetMapping
    public ResponseEntity<Page<SupplierResponse>>getAllSuppliers(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.getAllSuppliers(page,size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse>findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponse>update(@PathVariable Long id, @RequestBody SupplierUpdateRequest request){
      return  ResponseEntity.ok(service.update(id,request));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<SupplierResponse>>search(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String email,
            @RequestParam(required = false)String phone,
            @RequestParam(required = false)String address,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.search(name, email, phone, address, page, size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
