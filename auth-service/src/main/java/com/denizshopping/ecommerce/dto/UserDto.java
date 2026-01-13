package com.denizshopping.ecommerce.dto;

import com.denizshopping.ecommerce.entity.enums.Role;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {}