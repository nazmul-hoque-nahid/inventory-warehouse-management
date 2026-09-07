package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.ChangePasswordRequest;
import com.example.Inventory.Warehouse.Management.dto.request.LoginRequest;
import com.example.Inventory.Warehouse.Management.dto.response.AuthResponse;
import com.example.Inventory.Warehouse.Management.entity.User;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.UserRepository;
import com.example.Inventory.Warehouse.Management.security.CustomUserDetails;
import com.example.Inventory.Warehouse.Management.security.JwtService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthResponse login(LoginRequest request){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authentication =  authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        AuthResponse response=new AuthResponse();
        response.setToken(token);
        return response;
    }

}
