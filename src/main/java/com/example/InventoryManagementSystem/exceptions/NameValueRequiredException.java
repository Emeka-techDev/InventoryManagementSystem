package com.example.InventoryManagementSystem.exceptions;

public class NameValueRequiredException extends RuntimeException{
    public NameValueRequiredException(String message) {
        super(message);
    }
}
