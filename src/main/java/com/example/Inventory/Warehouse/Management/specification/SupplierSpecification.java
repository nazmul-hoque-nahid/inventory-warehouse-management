package com.example.Inventory.Warehouse.Management.specification;

import com.example.Inventory.Warehouse.Management.entity.Supplier;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SupplierSpecification {

    public static Specification<Supplier> search(
            String name,
            String email,
            String phone,
            String address
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Name
            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // Email
            if (email != null && !email.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
                        )
                );
            }

            // Phone
            if (phone != null && !phone.isBlank()) {
                predicates.add(
                        cb.like(
                                root.get("phone"),
                                "%" + phone + "%"
                        )
                );
            }

            // Address
            if (address != null && !address.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%"
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}