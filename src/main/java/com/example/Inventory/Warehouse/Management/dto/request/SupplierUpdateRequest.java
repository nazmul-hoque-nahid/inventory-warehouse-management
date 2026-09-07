package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierUpdateRequest {
    @Size(min = 5,max = 30)
    private String name;
    private String email;
    private String phone;
    private String address;
}
