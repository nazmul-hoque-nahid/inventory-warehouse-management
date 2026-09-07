package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.Inventory;
import com.example.Inventory.Warehouse.Management.entity.Product;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>, JpaSpecificationExecutor<Inventory> {
    boolean existsByWarehouseIdAndProductId(Long warehouseId, Long productId);
    boolean existsByQuantityGreaterThan(Integer quantity);
    Optional<Inventory> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
}
