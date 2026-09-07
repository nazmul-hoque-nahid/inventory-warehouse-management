package com.example.Inventory.Warehouse.Management.service;

import com.example.Inventory.Warehouse.Management.dto.request.*;
import com.example.Inventory.Warehouse.Management.dto.response.UserResponse;
import com.example.Inventory.Warehouse.Management.entity.User;
import com.example.Inventory.Warehouse.Management.exception.ResourceAlreadyExistsException;
import com.example.Inventory.Warehouse.Management.exception.ResourceNotFoundException;
import com.example.Inventory.Warehouse.Management.repository.UserRepository;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserResponse toResponse(User user){
        UserResponse response=new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setStatus(user.getStatus());
        response.setRole(user.getRole());
        return response;
    }
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public UserResponse create(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
          User user=new User();
          user.setName(request.getName());
          user.setEmail(request.getEmail());
          user.setStatus(User.Status.ACTIVE);
          user.setPhone(request.getPhone());
          user.setPassword(passwordEncoder.encode(request.getPassword()));
          user.setRole(request.getRole());
          return toResponse(userRepository.save(user));
    }
     public UserResponse findById(Long id){
         return toResponse(userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found")));
     }
     public Page<UserResponse> getAllUsers(int page, int size){
         Pageable pageable= PageRequest.of(page, size);
         return userRepository.findAll(pageable).map(this::toResponse);
     }
     public UserResponse update(Long id,UserUpdateRequest updateRequest){
         User user=userRepository.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("user not found"));
         if(user.getStatus()!= User.Status.ACTIVE){
             throw new BadRequestException("This user is not active");
         }
        if(updateRequest.getEmail()!=null && !updateRequest.getEmail().isBlank()){
            if(userRepository.existsByEmailAndIdNot(updateRequest.getEmail().trim(),id)){
                throw new ResourceAlreadyExistsException("This email already exist");
            }
            user.setEmail(updateRequest.getEmail().trim());
        }
        if(StringUtils.hasText(updateRequest.getName())){
            user.setName(updateRequest.getName().trim());
        }
        if(StringUtils.hasText(updateRequest.getPhone())){
            user.setPhone(updateRequest.getPhone().trim());
        }
        return toResponse(userRepository.save(user));
     }
     public UserResponse updateStatus(Long id, UserStatusUpdateRequest status){
         User user=userRepository.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("User not found"));
         if(status.getStatus()!=null){
            user.setStatus(status.getStatus());
         }
         return toResponse(userRepository.save(user));
     }
     public UserResponse updateRole(Long id, UserRoleUpdateRequest request){
         User user=userRepository.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("User not found"));
         if(request.getRole()!=null){
             user.setRole(request.getRole());
         }
         return toResponse(userRepository.save(user));
     }
     public void updateUserPassword(Long id,AdminPasswordUpdateRequest request){
         User user=userRepository.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("User not found"));
         user.setPassword(passwordEncoder.encode(request.getNewPassword()));
     }
    public void changePassword(Long id, ChangePasswordRequest request){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new BadRequestException("Password not matches");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
