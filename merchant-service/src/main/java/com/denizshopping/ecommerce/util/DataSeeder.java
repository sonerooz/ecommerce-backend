package com.denizshopping.ecommerce.util;

import com.denizshopping.ecommerce.entity.Store;
import com.denizshopping.ecommerce.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (storeRepository.count() == 0) {
            System.out.println("🌱 Merchant verileri ekleniyor...");

            Store store = new Store();
            store.setName("Deniz Tasarım Aksesuar");
            store.setSlug("deniz-tasarim-aksesuar");
            store.setCreatedBy("Admin");
            store.setLastModifiedBy("Admin");

            // Yeni Eklediğimiz Alanlar
            store.setEmail("info@deniztasarim.com");
            store.setTaxNumber("1234567890");
            store.setLogoUrl("https://cdn.deniztasarim.com/logo-main.png");
            store.setBannerUrl("https://cdn.deniztasarim.com/store-banner.jpg");

            storeRepository.save(store);

            System.out.println("✅ Mağaza oluşturuldu! ID: " + store.getId());
        }
    }
}