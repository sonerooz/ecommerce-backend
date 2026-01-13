package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Login için gerekli: Email ile kullanıcı bul
    Optional<User> findByEmail(String email);

    // Kayıt olurken gerekli: Bu email zaten var mı?
    boolean existsByEmail(String email);
}