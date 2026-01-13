package com.denizshopping.ecommerce.controller;

import com.denizshopping.ecommerce.dto.LoginRequest;
import com.denizshopping.ecommerce.dto.RegisterRequest;
import com.denizshopping.ecommerce.dto.TokenResponse;
import com.denizshopping.ecommerce.dto.UserDto;
import com.denizshopping.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}