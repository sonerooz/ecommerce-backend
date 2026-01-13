package com.denizshopping.ecommerce.dto;

import java.util.List;

public record ProductDto(
        Long id,
        String name,
        String description,
        Long storeId,
        List<CategoryDto> categories,
        List<ProductVariantDto> variants,
        List<ProductImageDto> images
) {}