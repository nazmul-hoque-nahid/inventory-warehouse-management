package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
Page<StockMovement>findByWarehouseId(Long warehouseId, Pageable pageable);
    Page<StockMovement>findByProductId(Long productId, Pageable pageable);
}