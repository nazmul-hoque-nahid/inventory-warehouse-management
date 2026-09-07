package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.PurchaseOrderItemRequest;
import com.example.Inventory.Warehouse.Management.dto.request.PurchaseOrderRequest;
import com.example.Inventory.Warehouse.Management.dto.response.PurchaseOrderItemResponse;
import com.example.Inventory.Warehouse.Management.dto.response.PurchaseOrderResponse;
import com.example.Inventory.Warehouse.Management.entity.*;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.exception.BadRequestException;
import com.example.Inventory.Warehouse.Management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {
    private PurchaseOrderResponse toResponse(PurchaseOrder order){
                 PurchaseOrderResponse response=new PurchaseOrderResponse();
                 response.setId(order.getId());
                 response.setOrderDate(order.getOrderDate());
                 response.setOrderedById(order.getOrderedBy().getId());
                 response.setOrderedByName(order.getOrderedBy().getName());
                 response.setSupplierId(order.getSupplier().getId());
                 response.setSupplierName(order.getSupplier().getName());
                 response.setWarehouseId(order.getWarehouse().getId());
                 response.setWarehouseName(order.getWarehouse().getName());
                 response.setStatus(order.getStatus());
                 List<PurchaseOrderItemResponse> responseList=new ArrayList<>();
                 BigDecimal totalAmount = BigDecimal.ZERO;
                 for(PurchaseOrderItem item:order.getItems()){
                     PurchaseOrderItemResponse itemResponse=new PurchaseOrderItemResponse();
                     itemResponse.setId(item.getId());
                     itemResponse.setProductId(item.getProduct().getId());
                     itemResponse.setProductName(item.getProduct().getName());
                     itemResponse.setQuantity(item.getQuantity());
                     itemResponse.setUnitPrice(item.getUnitPrice());
                     BigDecimal totalPrice =item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                     itemResponse.setTotalPrice(totalPrice);
                     totalAmount.add(totalPrice);
                     responseList.add(itemResponse);
                 }
                 response.setItems(responseList);
                 response.setTotalAmount(totalAmount);
                 return response;
    }
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    @Transactional
    public PurchaseOrderResponse create(Long currentUserId,PurchaseOrderRequest request){
        Warehouse warehouse=warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(()->new ResourceNotFoundException("Warehouse not found by id: "+request.getWarehouseId()));
        Supplier supplier=supplierRepository.findById(request.getSupplierId())
                .orElseThrow(()->new ResourceNotFoundException("Supplier not found by id: "+request.getSupplierId()));
        User orderedBy=userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(warehouse.getStatus()!=Warehouse.Status.ACTIVE){
            throw  new BadRequestException("warehouse not active");
        }
        if(orderedBy.getStatus()!=User.Status.ACTIVE){
            throw new BadRequestException("User is not active");
        }
        PurchaseOrder order=new PurchaseOrder();
        order.setWarehouse(warehouse);
        order.setSupplier(supplier);
        order.setOrderedBy(orderedBy);
        order.setStatus(PurchaseOrder.OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());
        Set<Long> productIds = new HashSet<>();
        for (PurchaseOrderItemRequest item : request.getItems()) {
            if (!productIds.add(item.getProductId())) {
                throw new BadRequestException(
                        "Product " + item.getProductId() +" appears more than once in the purchase order"
                );
            }
        }
        for (PurchaseOrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            order.getItems().add(item);
        }
        PurchaseOrder purchaseOrder=purchaseOrderRepository.save(order);
        return toResponse(purchaseOrder);
    }
    public Page<PurchaseOrderResponse>getAllPurchases(int page,int size){
        Pageable pageable= PageRequest.of(page, size);
        return purchaseOrderRepository.findAll(pageable).map(this::toResponse);
    }
    public PurchaseOrderResponse findById(Long id){
        return toResponse(purchaseOrderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found")));
    }

    @Transactional
    public PurchaseOrderResponse receive(Long currentUserId,Long id){
        PurchaseOrder order=purchaseOrderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Purchase Order Not found"));
        if(order.getStatus()!=PurchaseOrder.OrderStatus.CREATED){
            throw  new BadRequestException("Can't perform this operation because order may received or cancelled");
        }
        User user=userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!currentUserId.equals(order.getWarehouse().getManager().getId()) &&  user.getRole()!= User.Role.ADMIN){
            throw  new BadRequestException("this manager can't perform this operation.");
        }
        Warehouse warehouse=order.getWarehouse();
        for (PurchaseOrderItem item : order.getItems()) {
            Product product=item.getProduct();
            Integer quantity=item.getQuantity();
            Inventory inventory=inventoryRepository
                    .findByWarehouseIdAndProductId(warehouse.getId(),product.getId()).orElse(null);
            if (inventory==null){
                inventory=new Inventory();
                inventory.setWarehouse(warehouse);
                inventory.setProduct(product);
                inventory.setQuantity(quantity);
            } else {
                inventory.setQuantity(inventory.getQuantity()+quantity);
            }
            inventoryRepository.save(inventory);
            StockMovement movement=new StockMovement();
            movement.setWarehouse(warehouse);
            movement.setProduct(product);
            movement.setPerformedBy(user);
            movement.setMovementType(StockMovement.MovementType.IN);
            movement.setQuantity(quantity);
            movement.setMovementDate(LocalDateTime.now());
            movement.setNote("Stock received from Purchase Order: "+id);
            stockMovementRepository.save(movement);
        }
        order.setStatus(PurchaseOrder.OrderStatus.RECEIVED);
        order.setReceivedBy(user);
        PurchaseOrder savedOrder=purchaseOrderRepository.save(order);
        return toResponse(savedOrder);
    }
    @Transactional
    public PurchaseOrderResponse cancel(Long currentUserId,Long orderId) {
        PurchaseOrder order=purchaseOrderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Purchase order not found by id: "+orderId));
        if (order.getStatus()!=PurchaseOrder.OrderStatus.CREATED) {
            throw new BadRequestException("Only CREATED purchase orders can be cancelled");
        }
        User user=userRepository.findById(currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        order.setStatus(PurchaseOrder.OrderStatus.CANCELLED);
        order.setCancelledBy(user);
        return toResponse(order);
    }

}
