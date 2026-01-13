package com.denizshopping.ecommerce.repository;

import com.denizshopping.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Kategoriye göre ürünleri getir
    // (Product içindeki categories listesine bakar)
    List<Product> findByCategories_Id(Long categoryId);

    // Artık direkt "storeId" alanına bakıyoruz, alt nesneye (_Id) inmiyoruz.
    List<Product> findByStoreId(Long storeId);// Mağazaya göre ürünleri getir

    // İsim arama (Örn: "kolye" yazınca içinde kolye geçenleri bulur)
    List<Product> findByNameContainingIgnoreCase(String name);
}