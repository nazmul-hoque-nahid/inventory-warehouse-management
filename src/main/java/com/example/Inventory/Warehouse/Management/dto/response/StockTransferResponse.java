package com.example.Inventory.Warehouse.Management.dto.response;

import com.example.Inventory.Warehouse.Management.entity.StockTransfer;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StockTransferResponse {

    private Long id;
    private Long sourceWarehouseId;
    private String sourceWarehouseName;
    private Long destinationWarehouseId;
    private String destinationWarehouseName;
    private Long creatorId;
    private String creatorName;
    private StockTransfer.TransferStatus status;
    private LocalDateTime transferDate;
    private String note;
    private List<StockTransferItemResponse>itemResponseList=new ArrayList<>();
}