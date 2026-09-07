package com.example.Inventory.Warehouse.Management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Schema(
            description = "User's email address",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "wrong email format")
    private String email;

    @Schema(
            description = "User's password",
            example = "Password@123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8,
            maxLength = 100
    )
    @NotBlank(message = "password required")
    @Size(min = 8, max = 100,message = "minimum password length is 8")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    private String password;
}
