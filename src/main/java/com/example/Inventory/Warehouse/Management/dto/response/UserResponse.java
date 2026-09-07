package com.example.Inventory.Warehouse.Management.dto.response;

import com.example.Inventory.Warehouse.Management.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private User.Status status;
    private User.Role role;
}
