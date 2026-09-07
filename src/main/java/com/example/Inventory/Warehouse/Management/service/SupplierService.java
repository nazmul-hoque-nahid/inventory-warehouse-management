package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.SupplierRequest;
import com.example.Inventory.Warehouse.Management.dto.request.SupplierUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.SupplierResponse;
import com.example.Inventory.Warehouse.Management.entity.PurchaseOrder;
import com.example.Inventory.Warehouse.Management.entity.Supplier;
import com.example.Inventory.Warehouse.Management.exception.ResourceAlreadyExistsException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.PurchaseOrderRepository;
import com.example.Inventory.Warehouse.Management.repository.SupplierRepository;
import com.example.Inventory.Warehouse.Management.specification.SupplierSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private SupplierResponse toResponse(Supplier supplier){
        SupplierResponse response=new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setPhone(supplier.getPhone());
        return response;
    }
    private final SupplierRepository repository;
    private final PurchaseOrderRepository orderRepository;
   public SupplierResponse create(SupplierRequest request){
       Supplier supplier=new Supplier();
       supplier.setName(request.getName());
       supplier.setAddress(request.getAddress());
       supplier.setEmail(request.getEmail());
       supplier.setPhone(request.getPhone());
    return toResponse(repository.save(supplier));
   }
   public Page<SupplierResponse>getAllSuppliers(int page,int size){
       Pageable pageable= PageRequest.of(page,size);
       Page<Supplier>supplierPage=repository.findAll(pageable);
       return supplierPage.map(this::toResponse);
   }
   public SupplierResponse findById(Long id){
       Supplier supplier=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found "));
         return toResponse(supplier);
   }
   public SupplierResponse update(Long id,SupplierUpdateRequest request){
       Supplier supplier=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found id: "+id));
       if (request.getEmail() != null && !request.getEmail().isBlank()) {
           String email = request.getEmail().trim();
           if (repository.existsByEmailAndIdNot(email,id)) {
               throw new ResourceAlreadyExistsException(email+" already exists");
           }
           supplier.setEmail(email);
       }
       if(request.getAddress()!=null && !request.getAddress().isBlank()) {
           supplier.setAddress(request.getAddress().trim());
       }
       if(request.getName()!=null&& !request.getName().isBlank()){
           supplier.setName(request.getName().trim());
       }
       if(request.getPhone()!=null && !request.getPhone().isBlank()){
           supplier.setPhone(request.getPhone().trim());
       }
       return toResponse(repository.save(supplier));
   }
   public Page<SupplierResponse>search(String name,String email,String phone,String address, int page,int size){
       Pageable pageable=PageRequest.of(page, size);
       Specification<Supplier>specification= SupplierSpecification.search(name, email, phone, address);
       return repository.findAll(specification, pageable).map(this::toResponse);
   }
   public void delete(Long id){
       if(!repository.existsById(id)){throw new ResourceNotFoundException("Not found");}
       if(orderRepository.existsBySupplierId(id)){ throw new ResourceAlreadyExistsException("Can't delete Supplier");}
        repository.deleteById(id);

   }

}
