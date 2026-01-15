package com.denizshopping.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", indexes = {
        // 1. Path Index: "path LIKE '/1/%'" sorguları için (Ağaç yapısı performansı)
        @Index(name = "idx_category_path", columnList = "path"),

        // 2. Slug Index: "findBySlug" sorguları için (Storefront performansı)
        @Index(name = "idx_category_slug", columnList = "slug"),

        // 3. Parent ID Index: "WHERE parent_id = ?" ve JOIN sorguları için
        @Index(name = "idx_category_parent", columnList = "parent_id")
})
@SQLDelete(sql = "UPDATE categories SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String slug;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    // --- MATERIALIZED PATH ---
    // nullable = false : Bu alan asla boş kalamaz (Root olsa bile "/" olmalı)
    @Column(nullable = false)
    private String path;

    // --- HİYERARŞİ ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subCategories = new ArrayList<>();
}