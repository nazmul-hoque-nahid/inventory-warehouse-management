package com.example.Inventory.Warehouse.Management.specification;

import com.example.Inventory.Warehouse.Management.entity.Inventory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {

    public static Specification<Inventory> search(
            Long warehouseId,
            Long productId,
            Integer quantity
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (warehouseId != null) {
                predicates.add(
                        cb.equal(
                                root.get("warehouse").get("id"),
                                warehouseId
                        )
                );
            }

            if (productId != null) {
                predicates.add(
                        cb.equal(
                                root.get("product").get("id"),
                                productId
                        )
                );
            }

            if (quantity != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("quantity"),
                                quantity
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}