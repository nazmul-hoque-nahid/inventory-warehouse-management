package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.StockTransferItemRequest;
import com.example.Inventory.Warehouse.Management.dto.request.StockTransferRequest;
import com.example.Inventory.Warehouse.Management.dto.response.StockTransferItemResponse;
import com.example.Inventory.Warehouse.Management.dto.response.StockTransferResponse;
import com.example.Inventory.Warehouse.Management.dto.response.UserResponse;
import com.example.Inventory.Warehouse.Management.entity.*;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.*;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StockTransferService {
    private StockTransferResponse toResponse(StockTransfer stockTransfer) {
        StockTransferResponse response = new StockTransferResponse();
               response.setId(stockTransfer.getId());
               response.setDestinationWarehouseId(stockTransfer.getDestinationWarehouse().getId());
               response.setDestinationWarehouseName(stockTransfer.getDestinationWarehouse().getName());
               response.setSourceWarehouseId(stockTransfer.getSourceWarehouse().getId());
               response.setSourceWarehouseName(stockTransfer.getSourceWarehouse().getName());
               response.setCreatorId(stockTransfer.getCreatedBy().getId());
               response.setCreatorName(stockTransfer.getCreatedBy().getName());
               response.setNote(stockTransfer.getNote());
               response.setTransferDate(stockTransfer.getTransferDate());
               response.setStatus(stockTransfer.getStatus());
               List<StockTransferItemResponse>itemResponses=new ArrayList<>();
               for(StockTransferItem item:stockTransfer.getTransferItems()){
                   StockTransferItemResponse itemResponse=new StockTransferItemResponse();
                   itemResponse.setItemId(item.getId());
                   itemResponse.setProductId(item.getProduct().getId());
                   itemResponse.setProductName(item.getProduct().getName());
                   itemResponse.setQuantity(item.getQuantity());
                   itemResponses.add(itemResponse);
               }
       response.setItemResponseList(itemResponses);
        return response;
    }
    private final StockTransferRepository stockTransferRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final  InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    @Transactional
    public StockTransferResponse create(Long userId,StockTransferRequest request){
        if (request.getSourceWarehouseId().equals(request.getDestinationWarehouseId())) {
            throw new BadRequestException("Source and destination warehouses must be different");
        }
        Warehouse sourceWarehouse = warehouseRepository.findById(request.getSourceWarehouseId())
                .orElseThrow(()->new ResourceNotFoundException("Source warehouse not found by id: "+ request.getSourceWarehouseId()));
        User user=userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("user not found"));
        if(user.getStatus()!=User.Status.ACTIVE){
            throw new BadRequestException("This user is not active");
        }
        Warehouse destinationWarehouse = warehouseRepository.findById(request.getDestinationWarehouseId())
                .orElseThrow(()->new ResourceNotFoundException("Destination warehouse not found by id: "+ request.getDestinationWarehouseId()));
        if(sourceWarehouse.getStatus()!= Warehouse.Status.ACTIVE || destinationWarehouse.getStatus()!= Warehouse.Status.ACTIVE){
            throw new BadRequestException("Both warehouses most be on operation");
        }
        StockTransfer stockTransfer=new StockTransfer();
        Set<Long> productIds = new HashSet<>();
        for(StockTransferItemRequest item :request.getItems()){
            Long productId=item.getProductId();
            if (!productIds.add(productId)) {
                throw new BadRequestException("Duplicate products not allowed");
            }
            Inventory inventory=inventoryRepository.findByWarehouseIdAndProductId(request.getSourceWarehouseId(),item.getProductId())
                    .orElseThrow(()->new ResourceNotFoundException("warehous have no this products"));
            Integer availableQuantity=inventory.getQuantity() - inventory.getReservedQuantity();
            if(availableQuantity<item.getQuantity()){
                throw new BadRequestException("Insufficient Product quantity found");
            }
            inventory.setReservedQuantity(inventory.getReservedQuantity() + item.getQuantity());
            inventoryRepository.save(inventory);
            Product product=productRepository.findById(productId)
                    .orElseThrow(()->new ResourceNotFoundException("Product Not found"));
            StockTransferItem transferItem=new StockTransferItem();
            transferItem.setProduct(product);
            transferItem.setQuantity(item.getQuantity());
            transferItem.setStockTransfer(stockTransfer);
            stockTransfer.getTransferItems().add(transferItem);
        }
        stockTransfer.setCreatedBy(user);
        stockTransfer.setSourceWarehouse(sourceWarehouse);
        stockTransfer.setDestinationWarehouse(destinationWarehouse);
        stockTransfer.setNote(request.getNote());

        return toResponse(stockTransferRepository.save(stockTransfer));
    }
    @Transactional
    public Page<StockTransferResponse>getAll(int page,int size){
        Pageable pageable= PageRequest.of(page, size);
        return stockTransferRepository.findAll(pageable).map(this::toResponse);
    }
    @Transactional
    public StockTransferResponse findById(Long id){
        return toResponse(stockTransferRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found")));
    }
    @Transactional
    public StockTransferResponse complete(Long id,Long currentUserId){
        StockTransfer stockTransfer=stockTransferRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("SockTransfer doesn't exist"));
        if(stockTransfer.getStatus()!= StockTransfer.TransferStatus.PENDING){
            throw new BadRequestException("This transfer couldn't be update");
        }
        User user=userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!currentUserId.equals(stockTransfer.getDestinationWarehouse().getManager().getId()) &&  user.getRole()!= User.Role.ADMIN ){
            throw  new com.example.Inventory.Warehouse.Management.exception.BadRequestException("this manager can't perform this operation.");
        }
        for(StockTransferItem item:stockTransfer.getTransferItems()){
            Inventory sourceWarehouseInventory=inventoryRepository.findByWarehouseIdAndProductId(stockTransfer.getSourceWarehouse().getId(),item.getProduct().getId())
                    .orElseThrow(()->new ResourceNotFoundException("Not found source warehouse and product"));
            sourceWarehouseInventory.setQuantity(sourceWarehouseInventory.getQuantity()-item.getQuantity());
            sourceWarehouseInventory.setReservedQuantity(sourceWarehouseInventory.getReservedQuantity()-item.getQuantity());
            sourceWarehouseInventory.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(sourceWarehouseInventory);
            Inventory destinationWarehouseInventory=inventoryRepository.findByWarehouseIdAndProductId(stockTransfer.getDestinationWarehouse().getId(),item.getProduct().getId())
                    .orElse(null);
            if(destinationWarehouseInventory==null){
                Inventory inventory=new Inventory();
                inventory.setQuantity(item.getQuantity());
                inventory.setWarehouse(stockTransfer.getDestinationWarehouse());
                inventory.setProduct(item.getProduct());
                inventory.setLastUpdated(LocalDateTime.now());
                inventory.setLastUpdated(LocalDateTime.now());
                inventoryRepository.save(inventory);
            }else{
                destinationWarehouseInventory.setQuantity(destinationWarehouseInventory.getQuantity()+item.getQuantity());
                destinationWarehouseInventory.setLastUpdated(LocalDateTime.now());
                inventoryRepository.save(destinationWarehouseInventory);
            }
            StockMovement stockMovementOut=new StockMovement();
            StockMovement stockMovementIn=new StockMovement();
            stockMovementOut.setMovementType(StockMovement.MovementType.OUT);
            stockMovementOut.setPerformedBy(user);
            stockMovementOut.setStockTransfer(stockTransfer);
            stockMovementOut.setQuantity(item.getQuantity());
            stockMovementOut.setProduct(item.getProduct());
            stockMovementOut.setWarehouse(stockTransfer.getSourceWarehouse());

            stockMovementIn.setMovementType(StockMovement.MovementType.IN);
            stockMovementIn.setWarehouse(stockTransfer.getDestinationWarehouse());
            stockMovementIn.setPerformedBy(user);
            stockMovementIn.setProduct(item.getProduct());
            stockMovementIn.setQuantity(item.getQuantity());
            stockMovementIn.setStockTransfer(stockTransfer);
            stockMovementRepository.save(stockMovementIn);
            stockMovementRepository.save(stockMovementOut);

        }
        stockTransfer.setCompletedBy(user);
        stockTransfer.setStatus(StockTransfer.TransferStatus.COMPLETED);
        return toResponse(stockTransferRepository.save(stockTransfer));
    }
    @Transactional
    public StockTransferResponse cancel(Long id, Long currentUserId) {
        StockTransfer stockTransfer = stockTransferRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Stock transfer doesn't exist"));

        if (stockTransfer.getStatus()!= StockTransfer.TransferStatus.PENDING) {
            throw new BadRequestException("Only pending transfers can be cancelled");
        }
        User user=userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new BadRequestException("This user is not active");
        }
        if (!currentUserId.equals(stockTransfer.getDestinationWarehouse().getManager().getId())&& user.getRole() != User.Role.ADMIN) {
            throw new BadRequestException("You cannot cancel this transfer");
        }
        for(StockTransferItem item:stockTransfer.getTransferItems()){
            Inventory inventory = inventoryRepository
                    .findByWarehouseIdAndProductId(stockTransfer.getSourceWarehouse().getId(),item.getProduct().getId())
                    .orElseThrow(()->new ResourceNotFoundException("Source inventory not found"));

            if(inventory.getReservedQuantity()<item.getQuantity()){
                throw new BadRequestException("Invalid reserved quantity");
            }
            inventory.setReservedQuantity(inventory.getReservedQuantity()-item.getQuantity());
            inventory.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }
        stockTransfer.setStatus(StockTransfer.TransferStatus.CANCELLED);
        stockTransfer.setCancelledBy(user);
        return toResponse(stockTransferRepository.save(stockTransfer));
    }

}

