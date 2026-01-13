package com.denizshopping.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "product_variants")
@SQLDelete(sql = "UPDATE product_variants SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public class ProductVariant extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore // Serileştirme kazalarına karşı emniyet kemeri
    private Product product;

    // Fiyat burada, çünkü XL beden S bedenden pahalı olabilir
    @Column(nullable = false)
    private BigDecimal price;

    // Stok burada, çünkü Kırmızı bitmiş ama Mavi kalmış olabilir
    private Integer stockQuantity;

    // SKU (Stock Keeping Unit): Mağaza içi özel takip kodu
    // Örn: SL-IPEK-KIR-001
    @Column(unique = true)
    private String sku;

    // ÖZELLİKLER (PostgreSQL JSON Desteği)
    // Buraya {"renk": "Kırmızı", "beden": "M", "kumas": "Pamuk"} gibi istediğini atabilirsin.
    // Mağaza türüne göre özellikler değişse bile kod değiştirmene gerek kalmaz!
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;
}