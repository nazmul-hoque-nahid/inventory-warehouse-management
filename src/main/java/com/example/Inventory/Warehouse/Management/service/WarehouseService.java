package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.WarehouseRequest;
import com.example.Inventory.Warehouse.Management.dto.request.WarehouseStatusRequest;
import com.example.Inventory.Warehouse.Management.dto.request.WarehouseUpdateRequest;
import com.example.Inventory.Warehouse.Management.dto.response.UserResponse;
import com.example.Inventory.Warehouse.Management.dto.response.WarehouseResponse;
import com.example.Inventory.Warehouse.Management.entity.User;
import com.example.Inventory.Warehouse.Management.entity.Warehouse;
import com.example.Inventory.Warehouse.Management.exception.BadRequestException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.UserRepository;
import com.example.Inventory.Warehouse.Management.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private WarehouseResponse toResponse(Warehouse warehouse){
        WarehouseResponse response=new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setManagerId(warehouse.getManager().getId());
        response.setManagerName(warehouse.getManager().getName());
        response.setName(warehouse.getName());
        response.setLocation(warehouse.getLocation());
        response.setStatus(warehouse.getStatus());
        return response;
    }
    private final WarehouseRepository repository;
    private final UserRepository userRepository;
    public WarehouseResponse create(WarehouseRequest request){
        User manager=userRepository.findById(request.getManagerId())
                .orElseThrow(()->new ResourceNotFoundException("Manager  not found"));
        if(manager.getStatus()!= User.Status.ACTIVE)throw  new BadRequestException("This user not active");
        Warehouse warehouse=new Warehouse();
        warehouse.setManager(manager);
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setStatus(Warehouse.Status.ACTIVE);
        return toResponse(repository.save(warehouse));
    }
    public Page<WarehouseResponse>findAllWarehouses(int page,int size){
        Pageable pageable= PageRequest.of(page,size);
        return repository.findAll(pageable).map(this::toResponse);
    }
    public WarehouseResponse getById(Long id){
        return toResponse(repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Warehouse not found by id: "+id)));
    }
    public WarehouseResponse update(Long id, WarehouseUpdateRequest request){
        Warehouse warehouse = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Warehouse not found by id: " + id));
        if(request.getName()!=null && !request.getName().isBlank())warehouse.setName(request.getName());
        if(request.getLocation()!=null && !request.getLocation().isBlank())warehouse.setLocation(request.getLocation());
        return toResponse(repository.save(warehouse));
    }
    public WarehouseResponse updateStatus(Long id,WarehouseStatusRequest request){
        Warehouse warehouse = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Warehouse not found by id: " + id));
        warehouse.setStatus(request.getStatus());
        return toResponse(repository.save(warehouse));
    }
    public void delete(Long id){
        if(!repository.existsById(id))throw  new ResourceNotFoundException("Warehouse not found by id: "+id);
        repository.deleteById(id);
    }
}
