package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.Product;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    boolean existsBySkuAndIdNot(String sku,Long id);
    boolean existsBySku(String sku);
    boolean existsByCategoryId(Long categoryId);
}
