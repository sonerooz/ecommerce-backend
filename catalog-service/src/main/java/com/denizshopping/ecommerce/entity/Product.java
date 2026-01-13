package com.denizshopping.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
// "name" sütununa index atıyoruz ki arama hızlı olsun
@Table(name = "products", indexes = {
        @Index(name = "idx_product_name", columnList = "name")
})
@SQLDelete(sql = "UPDATE products SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public class Product extends BaseEntity{

    @Column(nullable = false)
    private String name; // Örn: "İpek Şal"

    @Column(length = 2000) // Uzun açıklama olabilir
    private String description;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    // Varyantlar (Zaten Vardı)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    // Resimler (BURAYI KONTROL ET)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();


    // KATEGORİLER (Bir ürün birden çok kategoride olabilir)
    // Örn: Hem "Setler" hem "Kolye" kategorisinde.
    @ManyToMany
    @JoinTable(
            name = "product_categories", // Ara tablonun adı
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}