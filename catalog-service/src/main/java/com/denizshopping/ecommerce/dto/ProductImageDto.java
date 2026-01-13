package com.denizshopping.ecommerce.dto;

public record ProductImageDto(
        Long id,
        String url,
        int displayOrder
) {}