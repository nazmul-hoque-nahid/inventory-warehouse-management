package com.example.Inventory.Warehouse.Management.controller;
import com.example.Inventory.Warehouse.Management.dto.request.LoginRequest;
import com.example.Inventory.Warehouse.Management.dto.response.AuthResponse;
import com.example.Inventory.Warehouse.Management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    private final AuthService service;

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token"
    )

    @ApiResponse(
            responseCode = "400",
            description = "Invalid login request",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid email or password",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "logged in successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)
            )
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@Valid@RequestBody LoginRequest request){
            return ResponseEntity.ok(service.login(request));
    }

}
