package com.denizshopping.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Auditing'i burada yapılandırıyoruz
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // İLERİDE BURASI DEĞİŞECEK!
        // Spring Security eklediğimizde: return () -> Optional.ofNullable(SecurityContextHolder.getContext()...);
        // Şimdilik geçici olarak sabit bir isim dönüyoruz:
        return () -> Optional.of("Admin");
    }
}