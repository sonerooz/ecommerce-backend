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
import java.util.ArrayList;
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
        // Eğer hiç kategori yoksa çalıştır
        if (categoryRepository.count() == 0) {
            System.out.println("🌱 Veritabanı boş, örnek veriler yükleniyor...");
            seedData();
            System.out.println("✅ Veri yükleme tamamlandı!");
        }
    }

    private void seedData() {
        // ---------------------------------------------------------
        // 1. KATEGORİ HİYERARŞİSİ (Takı -> Kolye -> Deniz Temalı)
        // ---------------------------------------------------------

        // A. Dede: "Takı"
        Category rootCat = new Category();
        rootCat.setName("Takı");
        rootCat.setSlug("taki");
        rootCat.setDescription("Tüm takı ürünleri");
        rootCat.setPath("/temp"); // ID oluşmadan path oluşmaz
        rootCat = categoryRepository.save(rootCat);

        // Path Güncelleme: /1
        rootCat.setPath("/" + rootCat.getId());
        categoryRepository.save(rootCat);

        // B. Baba: "Kolye" (Parent: Takı)
        Category subCat = new Category();
        subCat.setName("Kolye");
        subCat.setSlug("kolye");
        subCat.setParent(rootCat);
        subCat.setPath("/temp");
        subCat = categoryRepository.save(subCat);

        // Path Güncelleme: /1/2
        subCat.setPath(rootCat.getPath() + "/" + subCat.getId());
        categoryRepository.save(subCat);

        // C. Çocuk (Leaf): "Deniz Temalı" (Parent: Kolye)
        Category leafCat = new Category();
        leafCat.setName("Deniz Temalı");
        leafCat.setSlug("deniz-temali");
        leafCat.setParent(subCat);
        leafCat.setPath("/temp");
        leafCat = categoryRepository.save(leafCat);

        // Path Güncelleme: /1/2/3
        leafCat.setPath(subCat.getPath() + "/" + leafCat.getId());
        categoryRepository.save(leafCat);

        System.out.println("📂 Kategori Ağacı Oluşturuldu: " + leafCat.getPath());

        // ---------------------------------------------------------
        // 2. ÜRÜN OLUŞTURMA
        // ---------------------------------------------------------

        Product product = new Product();
        product.setName("Okyanus İncisi Deniz Kabuğu Seti");
        product.setDescription("Doğal inciler ve özel tasarım altın kaplama deniz kabuğu figürü.");
        product.setStoreId(1L); // Merchant ID (Mock)

        // KRİTİK: Ürün artık tek bir kategoriye (En alta) bağlı
        product.setCategories(List.of(leafCat));

        // ---------------------------------------------------------
        // 3. VARYANTLAR (Altın & Gümüş)
        // ---------------------------------------------------------

        // Varyant 1: Altın
        ProductVariant v1 = new ProductVariant();
        v1.setSku("OKYANUS-SET-ALTIN");
        v1.setPrice(new BigDecimal("450.00"));
        v1.setStockQuantity(100);
        v1.setProduct(product); // İlişkiyi kur

        Map<String, Object> attr1 = new HashMap<>();
        attr1.put("renk", "Altın");
        attr1.put("materyal", "Pirinç Üzeri Altın Kaplama");
        v1.setAttributes(attr1);

        // Varyant 2: Gümüş
        ProductVariant v2 = new ProductVariant();
        v2.setSku("OKYANUS-SET-GUMUS");
        v2.setPrice(new BigDecimal("420.00")); // Gümüş daha ucuz
        v2.setStockQuantity(50);
        v2.setProduct(product);

        Map<String, Object> attr2 = new HashMap<>();
        attr2.put("renk", "Gümüş");
        attr2.put("materyal", "925 Ayar Gümüş");
        v2.setAttributes(attr2);

        // Product entity içinde cascade varsa listeye ekleyebiliriz
        product.setVariants(List.of(v1, v2));

        // ---------------------------------------------------------
        // 4. RESİMLER
        // ---------------------------------------------------------
        ProductImage img1 = new ProductImage();
        img1.setUrl("https://placehold.co/600x800/png?text=Deniz+Seti+1");
        img1.setDisplayOrder(1);
        img1.setProduct(product);

        ProductImage img2 = new ProductImage();
        img2.setUrl("https://placehold.co/600x800/png?text=Deniz+Seti+2");
        img2.setDisplayOrder(2);
        img2.setProduct(product);

        product.setImages(List.of(img1, img2));

        // ---------------------------------------------------------
        // 5. KAYDET
        // ---------------------------------------------------------
        // CascadeType.ALL sayesinde varyantlar ve resimler de otomatik kaydedilir.
        productRepository.save(product);

        System.out.println("📦 Ürün Eklendi: " + product.getName());
    }
}