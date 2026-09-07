package com.example.Inventory.Warehouse.Management.repository;

import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long>{
    List<Warehouse>findByName(String name);
    boolean existsByName(String name);
    List<Warehouse>findByLocationContainingIgnoreCase(String location);
}
