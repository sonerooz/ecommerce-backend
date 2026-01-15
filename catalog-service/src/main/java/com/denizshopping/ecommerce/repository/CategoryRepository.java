package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 1. Sadece Ana Kategorileri Getir (Menüde dedeleri listelemek için)
    List<Category> findByParentIsNull();

    // 2. Ağaç Yapısı İçin (PERFORMANS SORGUSU)
    // Tek sorguda hem dedeleri hem de çocuklarını (subCategories) getirir.
    // "DISTINCT" tekrar eden kayıtları önler.
    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.subCategories WHERE c.parent IS NULL")
    List<Category> findAllWithSubCategories();

    // 3. Optional dönüyoruz çünkü belki kullanıcı URL'e rastgele bir şey yazdı
    Optional<Category> findBySlug(String slug);
}