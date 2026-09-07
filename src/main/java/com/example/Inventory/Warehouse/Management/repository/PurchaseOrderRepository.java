package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder>findByStatus(PurchaseOrder.OrderStatus status);
    List<PurchaseOrder>findBySupplierId(Long supplierId);
    List<PurchaseOrder>findByWarehouseId(Long warehouseId);
    List<PurchaseOrder>findByOrderedById(Long userId);
    boolean existsBySupplierId(Long id);
}