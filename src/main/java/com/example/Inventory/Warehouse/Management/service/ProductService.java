package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.ProductRequest;
import com.example.Inventory.Warehouse.Management.dto.request.ProductUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.ProductResponse;
import com.example.Inventory.Warehouse.Management.entity.Category;
import com.example.Inventory.Warehouse.Management.entity.Product;
import com.example.Inventory.Warehouse.Management.entity.PurchaseOrderItem;
import com.example.Inventory.Warehouse.Management.exception.ResourceAlreadyExistsException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.CategoryRepository;
import com.example.Inventory.Warehouse.Management.repository.InventoryRepository;
import com.example.Inventory.Warehouse.Management.repository.ProductRepository;
import com.example.Inventory.Warehouse.Management.repository.PurchaseOrderItemRepository;
import com.example.Inventory.Warehouse.Management.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderItemRepository orderItemRepository;
    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setUnitPrice(product.getUnitPrice());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        return response;
    }
    public ProductResponse create(ProductRequest request){
        Category category=categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found by id: "+request.getCategoryId()));
        if(productRepository.existsBySku(request.getSku())){
            throw new ResourceAlreadyExistsException("SKU "+request.getSku()+" already exist");
        }
        Product product=new Product();
        product.setName(request.getName());
        product.setCategory(category);
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        product.setUnitPrice(request.getUnitPrice());
       return toResponse(productRepository.save(product));
    }
    public Page<ProductResponse>getAllProducts(int page,int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Product>productPage=productRepository.findAll(pageable);
        return productPage.map(this::toResponse);
    }
    public ProductResponse findById(Long id){
        Product product=productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found by id: "+id));
        return toResponse(product);
    }
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow(()->
                        new ResourceNotFoundException("Product not found by id: " +id));

        if (request.getSku() != null && !request.getSku().isBlank()) {
            if (!request.getSku().equals(product.getSku()) && productRepository.existsBySkuAndIdNot(request.getSku(),id)) {
                throw new ResourceAlreadyExistsException("SKU already exists: " +request.getSku());
            }
            product.setSku(request.getSku().trim());
        }
        if (request.getName()!=null && !request.getName().isBlank()) {
            product.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription().trim());
        }

        if (request.getUnitPrice() != null) {
            product.setUnitPrice(request.getUnitPrice());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(()->new ResourceNotFoundException("Category not found by id: " +request.getCategoryId()));
            product.setCategory(category);
        }
        return toResponse(productRepository.save(product));
    }

    public  void  delete(Long id){
        if(!productRepository.existsById(id))throw  new ResourceNotFoundException("Product not found by id: "+id);
        if(inventoryRepository.existsById(id) || orderItemRepository.existsById(id))throw  new ResourceAlreadyExistsException("Resource Related to this product exist so can't delete");
        productRepository.deleteById(id);
    }
    public Page<ProductResponse> search(
            String name,
            String sku,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Product> specification =ProductSpecification.search(name,sku,categoryId,minPrice,maxPrice);
        return productRepository.findAll(specification, pageable).map(this::toResponse);
    }
}
