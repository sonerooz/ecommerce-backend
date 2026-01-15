package com.denizshopping.ecommerce.mapper;

import com.denizshopping.ecommerce.config.CentralMapperConfig;
import com.denizshopping.ecommerce.dto.CategoryDto;
import com.denizshopping.ecommerce.entity.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface CategoryMapper extends BaseMapper<CategoryDto, Category> {

    // --- 1. TREE MAPPING (Varsayılan - Ağaç Yapısı) ---
    // withTree=true dendiğinde bu çalışır. Çocukları recursive (kendini tekrar ederek) doldurur.
    @Override
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "subCategories", target = "subCategories")
    CategoryDto toDto(Category entity);

    // --- 2. SIMPLE MAPPING (Düz Liste - N+1 Korumalı) ---
    // withTree=false dendiğinde bu çalışır. Çocukları bilerek BOŞ geçer.

    @Named("toSimpleDto")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "subCategories", ignore = true) // <--- İşte sihir burada: Çocukları görmezden gel!
        // "parent" alanını ignore etmeye gerek yok çünkü DTO'da zaten yok.
    CategoryDto toSimpleDto(Category entity);

    @IterableMapping(qualifiedByName = "toSimpleDto") // Listeyi dönerken üsttekini kullan
    @Named("toSimpleDtoList")
    List<CategoryDto> toSimpleDtoList(List<Category> entities);

    // --- 3. DTO -> ENTITY (Yazma İşlemleri) ---
    @Override
    @Mapping(target = "parent", ignore = true)        // Service'te manuel set ediyoruz
    @Mapping(target = "subCategories", ignore = true) // Child ekleme buradan yapılmaz
    @Mapping(target = "path", ignore = true)          // Otomatik hesaplanıyor
    Category toEntity(CategoryDto dto);
}