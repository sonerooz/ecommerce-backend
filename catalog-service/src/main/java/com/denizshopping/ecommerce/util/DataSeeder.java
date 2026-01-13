package com.denizshopping.ecommerce.util;

import com.denizshopping.ecommerce.entity.Category;
import com.denizshopping.ecommerce.entity.Product;
import com.denizshopping.ecommerce.entity.ProductImage;
import com.denizshopping.ecommerce.entity.ProductVariant;
import com.denizshopping.ecommerce.repository.CategoryRepository;
import com.denizshopping.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            System.out.println("🌱 Catalog verileri ekleniyor...");

            // 1. Kategorileri Oluştur
            Category cat1 = new Category();
            cat1.setName("Kolye");
            cat1.setSlug("kolye");
            cat1.setCreatedBy("Admin");

            Category cat2 = new Category();
            cat2.setName("Deniz Temalı");
            cat2.setSlug("deniz-temali");
            cat2.setCreatedBy("Admin");

            categoryRepository.saveAll(List.of(cat1, cat2));

            // 2. Ürünü Oluştur
            Product product = new Product();
            product.setName("Okyanus İncisi Deniz Kabuğu Seti");
            product.setDescription("Doğal inciler ve özel tasarım altın kaplama deniz kabuğu figürü.");
            product.setCreatedBy("Admin");
            product.setLastModifiedBy("Admin");

            // KRİTİK NOKTA: Artık nesne değil, ID veriyoruz.
            // Merchant servisinde oluşan ilk mağazanın ID'si genelde 1 olur.
            product.setStoreId(1L);

            // Kategorileri bağla
            product.setCategories(List.of(cat1, cat2));

            // 3. Varyant Ekle (Altın Rengi)
            ProductVariant v1 = new ProductVariant();
            v1.setPrice(new BigDecimal("450.00"));
            v1.setStockQuantity(100);
            v1.setSku("OKYANUS-SET-ALTIN");
            v1.setCreatedBy("Admin");
            v1.setProduct(product);

            Map<String, Object> attr1 = new HashMap<>();
            attr1.put("renk", "Altın");
            attr1.put("materyal", "Pirinç Üzeri Altın Kaplama");
            v1.setAttributes(attr1);

            // 4. Varyant Ekle (Gümüş Rengi)
            ProductVariant v2 = new ProductVariant();
            v2.setPrice(new BigDecimal("420.00"));
            v2.setStockQuantity(50);
            v2.setSku("OKYANUS-SET-GUMUS");
            v2.setCreatedBy("Admin");
            v2.setProduct(product);

            Map<String, Object> attr2 = new HashMap<>();
            attr2.put("renk", "Gümüş");
            attr2.put("materyal", "925 Ayar Gümüş");
            v2.setAttributes(attr2);

            product.setVariants(List.of(v1, v2));

            // 5. Resimleri Ekle
            ProductImage img1 = new ProductImage();
            img1.setUrl("https://cdn.deniztasarim.com/img1.jpg");
            img1.setDisplayOrder(1);
            img1.setCreatedBy("Admin");
            img1.setProduct(product);

            ProductImage img2 = new ProductImage();
            img2.setUrl("https://cdn.deniztasarim.com/img2.jpg");
            img2.setDisplayOrder(2);
            img2.setCreatedBy("Admin");
            img2.setProduct(product);

            product.setImages(List.of(img1, img2));

            // Kaydet
            productRepository.save(product);

            System.out.println("✅ Örnek ürün kataloğa eklendi: " + product.getName());
        }
    }
}