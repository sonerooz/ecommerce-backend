package com.denizshopping.ecommerce.dto;

public record StoreDto(
        Long id,
        String name,
        String slug,
        String email,
        String taxNumber,
        String logoUrl,
        String bannerUrl
) {}