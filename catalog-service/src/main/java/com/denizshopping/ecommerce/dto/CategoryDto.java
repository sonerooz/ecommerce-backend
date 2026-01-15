package com.denizshopping.ecommerce.dto;

import java.util.List;

public record CategoryDto(
        Long id,

        // Validasyon eklemekte fayda var (spring-boot-starter-validation varsa)
        // @NotBlank(message = "Kategori adı boş olamaz")
        String name,

        String slug,          // URL için (Boş gelirse Service'te name'den üretilecek)
        String description,   // SEO ve Admin bilgi notu
        String imageUrl,      // Görsel linki

        // --- HİYERARŞİ İÇİN GEREKLİ ALANLAR ---

        Long parentId,        // INPUT: Yeni eklerken "Bunun babası kim?" (Admin için)

        String path,          // OUTPUT: Frontend bilsin diye (Örn: /1/5/12) - Sadece okunur.

        List<CategoryDto> subCategories // OUTPUT: Menü Ağacı (Storefront için)
) {}