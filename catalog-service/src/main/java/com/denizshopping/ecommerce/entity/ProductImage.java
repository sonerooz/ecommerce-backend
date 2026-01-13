package com.denizshopping.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "product_images")
@SQLDelete(sql = "UPDATE product_images SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public class ProductImage extends BaseEntity{

    private String url; // Resmin internet adresi

    private int displayOrder; // Gösterim sırası (1. resim, 2. resim...)

    // --- EKSİK OLAN KISIM BURASIYDI ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // Veritabanındaki FK kolonunun adı
    @JsonIgnore // Sonsuz döngüyü engellemek için (Jackson tarafında)
    private Product product;
}