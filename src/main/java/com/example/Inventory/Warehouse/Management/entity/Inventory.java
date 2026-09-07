package com.example.Inventory.Warehouse.Management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"warehouse_id", "product_id"}
        )
)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "warehouse_id",nullable = false)
    private Warehouse warehouse;
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    @Version
    private Long version;
    @Min(0)
    @Column(nullable = false)
    private Integer quantity=0;
    @Min(0)
    private Integer reservedQuantity=0;
    private LocalDateTime lastUpdated;

}