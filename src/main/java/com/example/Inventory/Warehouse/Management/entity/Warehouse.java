package com.example.Inventory.Warehouse.Management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status=Status.ACTIVE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id",unique = true)
    private User manager;
    public enum Status{
        ACTIVE,
        PERMANENTLY_CLOSED,
        TEMPORARILY_CLOSED
    }
}
