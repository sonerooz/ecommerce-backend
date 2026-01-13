package com.denizshopping.ecommerce.entity;

import com.denizshopping.ecommerce.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "users") // DİKKAT: 'user' yapma, Postgres kızar!
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Şifrelenmiş (BCrypt) olarak saklanacak

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;
}