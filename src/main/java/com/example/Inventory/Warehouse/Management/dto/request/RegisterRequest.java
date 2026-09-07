package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.User;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "email required")
    @Email(message = "wrong email format")
    private String email;
    @NotBlank(message = "phone is required")
    private String phone;
    @NotBlank(message = "password required")
    @Size(min = 8, max = 100)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    private String password;
    @NotNull(message = "role required")
    private User.Role role;
}
