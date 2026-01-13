package com.denizshopping.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariantDto(
        Long id,
        BigDecimal price,
        Integer stockQuantity,
        String sku,
        Map<String, Object> attributes
) {}