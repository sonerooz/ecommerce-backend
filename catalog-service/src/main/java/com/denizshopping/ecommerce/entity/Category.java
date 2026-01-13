package com.denizshopping.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter // @Data yerine Getter/Setter kullanıyoruz, sonsuz döngüden kaçınmak için
public class Category extends BaseEntity{

    @Column(nullable = false)
    private String name; // Örn: "Kolye"

    @Column(unique = true)
    private String slug; // Örn: "kolye" veya "taki-setleri"

    private String description;

    private String imageUrl; // Kategori resmi

    // --- AĞAÇ YAPISI (HIERARCHY) ---
    // Üst Kategori (Annesi)
    // Eğer bu null ise, bu bir "Ana Kategori"dir (Örn: Takı).
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // Alt Kategoriler (Çocukları)
    // Örn: Takı -> [Kolye, Küpe, Bileklik]
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subCategories;

    // --- ÜRÜN İLİŞKİSİ ---

    // Bir kategori içinde binlerce ürün olabilir.
    // "mappedBy" diyerek ilişkinin yönetimini Product tarafına veriyoruz.
    @ManyToMany(mappedBy = "categories")
    private List<Product> products;
}