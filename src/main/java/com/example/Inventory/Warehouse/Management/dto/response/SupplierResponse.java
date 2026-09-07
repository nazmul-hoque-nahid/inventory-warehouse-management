package com.example.Inventory.Warehouse.Management.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
