package com.example.Inventory.Warehouse.Management.controller;

import com.example.Inventory.Warehouse.Management.dto.request.*;
import com.example.Inventory.Warehouse.Management.dto.response.UserResponse;
import com.example.Inventory.Warehouse.Management.security.CustomUserDetails;
import com.example.Inventory.Warehouse.Management.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse>create(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse>getById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>>getAllUsers(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return ResponseEntity.ok(service.getAllUsers(page, size));
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponse>update(@Valid@PathVariable Long id,@RequestBody UserUpdateRequest updateRequest){
        return ResponseEntity.ok(service.update(id,updateRequest));
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse>updateStatus(@PathVariable Long id, @Valid@RequestBody UserStatusUpdateRequest statusUpdateRequest){
       return ResponseEntity.ok(service.updateStatus(id,statusUpdateRequest));
    }
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void>updatePassword(@PathVariable Long id, @Valid@RequestBody AdminPasswordUpdateRequest request){
        service.updateUserPassword(id,request);
      return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse>updateRole(@PathVariable Long id, @Valid@RequestBody UserRoleUpdateRequest roleUpdateRequest){
        return ResponseEntity.ok(service.updateRole(id,roleUpdateRequest));
    }
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        service.changePassword(principal.getId(), request);
        return ResponseEntity.noContent().build();
    }

}
