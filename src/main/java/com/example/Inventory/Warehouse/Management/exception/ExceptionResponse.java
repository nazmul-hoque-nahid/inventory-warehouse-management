package com.example.Inventory.Warehouse.Management.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ExceptionResponse {
    private Integer status;
    private String message;
    private LocalDateTime atTime;

    public ExceptionResponse(int status,String message,LocalDateTime now) {
        this.status=status;
        this.message=message;
        this.atTime=now;
    }
}
