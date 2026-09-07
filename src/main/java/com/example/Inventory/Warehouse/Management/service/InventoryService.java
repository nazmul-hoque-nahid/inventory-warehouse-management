package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.InventoryRequest;
import com.example.Inventory.Warehouse.Management.dto.response.InventoryResponse;
import com.example.Inventory.Warehouse.Management.entity.Inventory;
import com.example.Inventory.Warehouse.Management.entity.Product;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import com.example.Inventory.Warehouse.Management.exception.ResourceAlreadyExistsException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.InventoryRepository;
import com.example.Inventory.Warehouse.Management.repository.ProductRepository;
import com.example.Inventory.Warehouse.Management.repository.WarehouseRepository;
import com.example.Inventory.Warehouse.Management.specification.InventorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private InventoryResponse toResponse(Inventory inventory){
        InventoryResponse response=new InventoryResponse();
        response.setId(inventory.getId());
        response.setWarehouseId(inventory.getWarehouse().getId());
        response.setWarehouseName(inventory.getWarehouse().getName());
        response.setProductId(inventory.getProduct().getId());
        response.setProductName(inventory.getProduct().getName());
        response.setQuantity(inventory.getQuantity());
        return response;
    }
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public Page<InventoryResponse> getAllInventories(int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        return inventoryRepository.findAll(pageable).map(this::toResponse);
    }
    public InventoryResponse findById(Long id){
        return toResponse(inventoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Not found")));
    }
    public Page<InventoryResponse> search(
            Long warehouseId,
            Long productId,
            Integer quantity,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Inventory> specification =InventorySpecification.search(warehouseId,productId,quantity);
        return inventoryRepository.findAll(specification, pageable).map(this::toResponse);
    }
    public void delete(Long id){
        if(!inventoryRepository.existsById(id))throw new ResourceNotFoundException("Not found by id "+id);
        if(inventoryRepository.existsByQuantityGreaterThan(0))throw  new ResourceAlreadyExistsException("Can't delete this warehouse");
        inventoryRepository.deleteById(id);
    }
}
