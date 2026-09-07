package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRoleUpdateRequest {
    private User.Role role;
}
