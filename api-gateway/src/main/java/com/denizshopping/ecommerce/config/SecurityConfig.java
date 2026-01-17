package com.denizshopping.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebFluxSecurity // DİKKAT: Gateway "Reactive" olduğu için bu anotasyon şart
public class SecurityConfig {

    // Auth Service ile AYNI secret key olmalı!
    // Genelde application.yml'dan çekilir ama şimdilik hardcoded yapalım çalışsın.
    // Auth Service'deki JwtService sınıfındaki SECRET_KEY neyse buraya onu yapıştır.
    private final String jwtSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder()))); // Decoder'ı buraya el ile veriyoruz

        return http.build();
    }

    // 👇 EKSİK OLAN BEAN BUYDU 👇
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] bytes = io.jsonwebtoken.io.Decoders.BASE64.decode(jwtSecret);
        SecretKeySpec originalKey = new SecretKeySpec(bytes, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(originalKey).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}