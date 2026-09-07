package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.response.StockMovementResponse;
import com.example.Inventory.Warehouse.Management.entity.StockMovement;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockMovementService {
    private StockMovementResponse toResponse(StockMovement movement){
        StockMovementResponse response=new StockMovementResponse();
        response.setId(movement.getId());
        response.setMovementType(movement.getMovementType());
        response.setMovementDate(movement.getMovementDate());
        response.setWarehouseId(movement.getWarehouse().getId());
        response.setWarehouseName(movement.getWarehouse().getName());
        response.setProductId(movement.getProduct().getId());
        response.setProductName(movement.getProduct().getName());
        response.setPerformedById(movement.getPerformedBy().getId());
        response.setPerformedByName(movement.getPerformedBy().getName());
        response.setQuantity(movement.getQuantity());
        response.setNote(movement.getNote());
        response.setStockTransferId(movement.getStockTransfer().getId());

        return response;
    }
    private final StockMovementRepository stockMovementRepository;

    public Page<StockMovementResponse> getAll(int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        return stockMovementRepository.findAll(pageable).map(this::toResponse);
    }
    public StockMovementResponse getById(Long id){
        return toResponse(stockMovementRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Not found")));
    }
    public Page<StockMovementResponse> getByWarehouse(Long warehouseId,int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        return stockMovementRepository.findByWarehouseId(warehouseId,pageable).map(this::toResponse);
    }
    public Page<StockMovementResponse> getByProduct(Long productId,int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        return stockMovementRepository.findByProductId(productId,pageable).map(this::toResponse);
    }
}
