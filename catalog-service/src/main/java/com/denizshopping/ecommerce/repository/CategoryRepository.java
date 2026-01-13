package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Ana kategorileri getir (Parent'ı NULL olanlar)
    // Örn: Sadece "Takı", "Giyim" gelir; "Kolye" gelmez.
    List<Category> findByParentIsNull();

    // URL'den kategori bul
    Category findBySlug(String slug);
}