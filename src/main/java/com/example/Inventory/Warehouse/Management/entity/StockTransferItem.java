package com.example.Inventory.Warehouse.Management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "stock_transfer_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transfer_product",
                        columnNames = {"transfer_id", "product_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class StockTransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "transfer_id",nullable = false)
    private StockTransfer stockTransfer;
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;
}
