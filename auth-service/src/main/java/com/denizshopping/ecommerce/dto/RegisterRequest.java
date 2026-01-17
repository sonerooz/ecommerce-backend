package com.denizshopping.ecommerce.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role // <--- YENİ EKLENDİ (Opsiyonel olsun)
) {}