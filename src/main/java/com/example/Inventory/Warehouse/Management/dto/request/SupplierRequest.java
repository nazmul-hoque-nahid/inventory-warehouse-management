package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {
    @NotBlank(message = "name required")
    @Size(min = 5,max = 30)
    private String name;
    @NotBlank(message = "email  required")
    @Email(message = "Wrong email format")
    private String email;
    @NotBlank(message = "phone required")
    private String phone;
    @NotBlank(message = "address required")
    private String address;
}
