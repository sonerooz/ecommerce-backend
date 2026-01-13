package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // 1. JSON içindeki 'renk' alanına göre arama
    @Query(value = "SELECT * FROM product_variants v WHERE v.attributes ->> 'renk' = :color", nativeQuery = true)
    List<ProductVariant> findByColor(@Param("color") String color);

    // 2. JSON içindeki 'beden' alanına göre arama
    @Query(value = "SELECT * FROM product_variants v WHERE v.attributes ->> 'beden' = :size", nativeQuery = true)
    List<ProductVariant> findBySize(@Param("size") String size);

    // 3. Bir ana ürüne ait varyantları getir
    List<ProductVariant> findByProductId(Long productId);
}