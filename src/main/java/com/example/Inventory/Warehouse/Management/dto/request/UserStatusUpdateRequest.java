package com.example.Inventory.Warehouse.Management.dto.request;

import com.example.Inventory.Warehouse.Management.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusUpdateRequest {
    private User.Status status;
}
