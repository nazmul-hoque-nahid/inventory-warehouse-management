package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.StockTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferItemRepository extends JpaRepository<StockTransferItem,Long> {
}
