package com.example.InventoryManagementSystem;
import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) {
        String rawKey = "sweetCodes123456789sweetCodes123456789sweetCodes123456789";
        String base64Key = Base64.getEncoder().encodeToString(rawKey.getBytes());
        System.out.println("Base64 Encoded Key: " + base64Key);
    }
}

