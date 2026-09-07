package com.example.Inventory.Warehouse.Management.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role=Role.WAREHOUSE_STAFF;
    public enum Status{
        ACTIVE,
        INACTIVE
    }
    public enum Role {
        ADMIN,
        INVENTORY_MANAGER,
        WAREHOUSE_MANAGER,
        WAREHOUSE_STAFF
    }
}
