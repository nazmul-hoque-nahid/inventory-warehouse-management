package com.example.Inventory.Warehouse.Management.specification;
import com.example.Inventory.Warehouse.Management.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> search(
            String name,
            String sku,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Product name
            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // SKU
            if (sku != null && !sku.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("sku")),
                                "%" + sku.toLowerCase() + "%"
                        )
                );
            }

            // Category ID
            if (categoryId != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );
            }

            // Minimum price
            if (minPrice != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("unitPrice"),
                                minPrice
                        )
                );
            }

            // Maximum price
            if (maxPrice != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("unitPrice"),
                                maxPrice
                        )
                );
            }
            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}