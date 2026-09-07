package com.example.Inventory.Warehouse.Management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_transfers")
@Getter
@Setter
@NoArgsConstructor
public class StockTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_warehouse_id", nullable = false)
    private Warehouse sourceWarehouse;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_warehouse_id", nullable = false)
    private Warehouse destinationWarehouse;
    @OneToMany(mappedBy = "stockTransfer",cascade=CascadeType.ALL)
    private List<StockTransferItem> transferItems=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "completed_by")
    private User completedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status=TransferStatus.PENDING;
    @Column(nullable = false)
    @CurrentTimestamp
    private LocalDateTime transferDate;
    private String note;
    @Version
    private Long version;
    public enum TransferStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}