package com.denizshopping.ecommerce.service;

import com.denizshopping.ecommerce.dto.CategoryDto;
import com.denizshopping.ecommerce.entity.Category;
import com.denizshopping.ecommerce.mapper.CategoryMapper;
import com.denizshopping.ecommerce.repository.CategoryRepository;
import com.denizshopping.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(boolean withTree) {
        if (withTree) {
            // AĞAÇ: Tek sorguda (JOIN FETCH) hepsini çek + Recursive Map
            List<Category> rootCategories = categoryRepository.findAllWithSubCategories();
            return categoryMapper.toDtoList(rootCategories);
        } else {
            // DÜZ LİSTE: Sadece kategorileri çek + Çocukları Map ETME (SimpleDto)
            // Bu sayede N+1 olmaz, sorgu sayısı 1'e düşer.
            List<Category> allCategories = categoryRepository.findAll();
            return categoryMapper.toSimpleDtoList(allCategories);
        }
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);

        // 1. Slug Otomatik Üretim
        if (!StringUtils.hasText(category.getSlug())) {
            category.setSlug(toSlug(category.getName()));
        }

        // 2. Parent Atama
        if (categoryDto.parentId() != null) {
            Category parent = categoryRepository.findById(categoryDto.parentId())
                    .orElseThrow(() -> new RuntimeException("Üst kategori bulunamadı!"));
            category.setParent(parent);
        }

        // 3. Path Oluşturma (Önce kaydet ID al, sonra path güncelle)
        category.setPath("/temp");
        categoryRepository.save(category);
        updateCategoryPath(category);

        return categoryMapper.toDto(category);
    }

    // --- UPDATE (Admin) ---
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek kategori bulunamadı!"));

        // 1. İsim Güncelleme
        if (StringUtils.hasText(categoryDto.name())) {
            existingCategory.setName(categoryDto.name());
        }

        // 2. Açıklama Güncelleme
        if (StringUtils.hasText(categoryDto.description())) {
            existingCategory.setDescription(categoryDto.description());
        }

        // 3. Resim Güncelleme
        if (StringUtils.hasText(categoryDto.imageUrl())) {
            existingCategory.setImageUrl(categoryDto.imageUrl());
        }

        // 4. Slug Güncelleme (Manuel Müdahale Varsa)
        // Eğer Admin özellikle yeni bir slug gönderdiyse onu işleriz.
        // Ama boş gönderdiyse ESKİ SLUG kalır (SEO Koruma).
        if (StringUtils.hasText(categoryDto.slug()) && !categoryDto.slug().equals(existingCategory.getSlug())) {
            existingCategory.setSlug(toSlug(categoryDto.slug()));
        }

        // NOT: Parent değişikliği (Kategoriyi taşıma) burada yapılmaz.
        // O işlem için "moveCategory" adında özel bir metot yazmak gerekir (Path hesapları için).

        Category savedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(savedCategory);
    }

    // --- READ (Single by ID) - Admin İçin ---
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));
        return categoryMapper.toDto(category);
    }

    // --- READ (Single) ---
    @Transactional(readOnly = true)
    public CategoryDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!")); // Burada Custom 404 Exception fırlatmak daha şık olur ileride.

        return categoryMapper.toDto(category);
    }

    // --- HELPERS ---
    private void updateCategoryPath(Category category) {
        if (category.getParent() != null) {
            category.setPath(category.getParent().getPath() + "/" + category.getId());
        } else {
            category.setPath("/" + category.getId());
        }
    }

    private String toSlug(String input) {
        if (input == null) return "";
        String nonLatin = Pattern.compile("[^\\w-]")
                .matcher(Normalizer.normalize(input, Normalizer.Form.NFD))
                .replaceAll("");
        return nonLatin.toLowerCase(Locale.ENGLISH).replace(" ", "-");
    }
}