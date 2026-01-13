package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // URL'den mağaza bulmak için (Örn: deniz-aksesuar)
    Optional<Store> findBySlug(String slug);
}