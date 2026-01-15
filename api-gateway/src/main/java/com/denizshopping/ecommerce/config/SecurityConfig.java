package com.denizshopping.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity // DİKKAT: Gateway "Reactive" olduğu için bu anotasyon şart
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // 1. CSRF'i kapat (API'lerde genelde kapalı olur)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // 2. CORS Ayarlarını Yükle (Next.js'in hata almaması için)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. İzinler (Kritik Nokta Burası)
                .authorizeExchange(exchanges -> exchanges
                        // Kategorileri Herkese Aç (Login Gerekmesin)
                        .pathMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        // Ürünleri Herkese Aç
                        .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        // Auth (Login/Register) endpointlerini Herkese Aç
                        .pathMatchers("/api/auth/**").permitAll()

                        // Geri kalan her yer için Login şart olsun
                        .anyExchange().authenticated()
                )
                // Basic Auth ve Form Login'i şimdilik kapatıyoruz
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        return http.build();
    }

    // Frontend (Next.js) ile haberleşebilmek için CORS ayarı
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Şimdilik her yerden gelen isteği kabul et
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}