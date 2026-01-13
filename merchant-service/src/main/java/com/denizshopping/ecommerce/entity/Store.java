package com.denizshopping.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "stores")
@SQLDelete(sql = "UPDATE stores SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
public class Store extends BaseEntity {

    private String name;
    private String slug;

    // Yeni Eklenenler
    private String logoUrl;
    private String bannerUrl;
    private String taxNumber;
    private String email;
}