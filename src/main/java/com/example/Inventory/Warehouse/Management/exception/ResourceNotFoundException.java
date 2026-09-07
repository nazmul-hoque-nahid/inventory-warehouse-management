package com.example.Inventory.Warehouse.Management.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
       super(message);
    }
}
