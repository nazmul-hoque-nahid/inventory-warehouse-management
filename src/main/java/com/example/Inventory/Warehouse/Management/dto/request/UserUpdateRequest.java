package com.example.Inventory.Warehouse.Management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Pattern(regexp = "^[a-zA-Z ]+$",message = "name only supports characters with space")
    private String name;
    @Email(message = "invalid email formating")
    private String email;
    private String phone;
}
