package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.CategoryRequest;
import com.example.Inventory.Warehouse.Management.dto.request.CategoryUpdateRequest;
import com.example.Inventory.Warehouse.Management.entity.Category;
import com.example.Inventory.Warehouse.Management.exception.ResourceAlreadyExistsException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.CategoryRepository;
import com.example.Inventory.Warehouse.Management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final ProductRepository productRepository;
    public Category create(CategoryRequest request){
        Category category=new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return repository.save(category);
    }
    public Page<Category>getAllCategories(int page,int size){
        Pageable pageable= PageRequest.of(page,size);
        return repository.findAll(pageable);
    }
    public Category findById(Long id){
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));
    }
    public Category update(Long id, CategoryUpdateRequest request){
       Category category=repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));
        if (request.getName() != null && !request.getName().isBlank()) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            category.setDescription(request.getDescription());
        }
       return repository.save(category);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        if (productRepository.existsByCategoryId(id)) {
            throw new ResourceAlreadyExistsException("Cannot delete category because products are assigned to it");
        }
        repository.deleteById(id);
    }
}
