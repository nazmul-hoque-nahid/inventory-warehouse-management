package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.CategoryRequest;
import com.example.Inventory.Warehouse.Management.dto.request.CategoryUpdateRequest;
import com.example.Inventory.Warehouse.Management.entity.Category;
import com.example.Inventory.Warehouse.Management.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Product Category Management")
@PreAuthorize("hasAnyRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    private final CategoryService service;
    @Operation(
            summary = "Create category"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Category.class)
            )
    )
    @PostMapping
    public ResponseEntity<Category> create(@Valid@RequestBody CategoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @GetMapping
    public ResponseEntity<Page<Category>>getAllCategories(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
       return ResponseEntity.ok(service.getAllCategories(page,size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category>getById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Category>update(@PathVariable Long id,@Valid@RequestBody CategoryUpdateRequest request){
        return ResponseEntity.ok(service.update(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}

