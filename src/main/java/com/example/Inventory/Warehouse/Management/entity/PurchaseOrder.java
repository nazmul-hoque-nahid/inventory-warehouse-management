package com.example.Inventory.Warehouse.Management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.sql.results.graph.Fetch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

@Setter
@Getter
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "supplier_id",nullable = false)
    private Supplier supplier;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ordered_by",nullable = false)
    private User orderedBy;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;
    @Column(nullable = false)
    private LocalDateTime orderDate;
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @Enumerated(EnumType.STRING)
    private OrderStatus status=OrderStatus.CREATED;
    @OneToMany(mappedBy = "purchaseOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();
    public enum OrderStatus{
        CREATED,
        RECEIVED,
        CANCELLED
    }
}