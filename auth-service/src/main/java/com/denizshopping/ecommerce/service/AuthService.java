package com.denizshopping.ecommerce.service;

import com.denizshopping.ecommerce.dto.LoginRequest;
import com.denizshopping.ecommerce.dto.RegisterRequest;
import com.denizshopping.ecommerce.dto.TokenResponse;
import com.denizshopping.ecommerce.dto.UserDto;
import com.denizshopping.ecommerce.entity.User;
import com.denizshopping.ecommerce.entity.enums.Role;
import com.denizshopping.ecommerce.mapper.UserMapper;
import com.denizshopping.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    // userDetailsService bağımlılığını buradan kaldırabilirsin, gerek yok.

    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(Role.ROLE_CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.password()));

        return userMapper.toDto(userRepository.save(user));
    }

    public TokenResponse login(LoginRequest request) {
        // 1. Doğrula
        // Bu işlem sırasında Spring Security otomatik olarak CustomUserDetailsService'e gider,
        // şifreyi BCrypt ile kontrol eder. Hata varsa exception fırlatır.
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Başarılıysa UserDetails'i al (DB'ye tekrar gitmeye gerek yok)
        var userDetails = (UserDetails) auth.getPrincipal();

        // 3. Token üret
        String token = jwtService.generateToken(userDetails);

        return new TokenResponse(token);
    }
}