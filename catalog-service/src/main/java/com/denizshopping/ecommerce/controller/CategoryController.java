package com.denizshopping.ecommerce.controller;

import com.denizshopping.ecommerce.dto.CategoryDto;
import com.denizshopping.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. Tüm Liste (Tree veya Düz)
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "true") boolean withTree) {
        return ResponseEntity.ok(categoryService.getAllCategories(withTree));
    }

    // 2. ID ile Getir (Admin - Edit Sayfası İçin)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // 3. Slug ile Getir (Storefront - Detay Sayfası İçin)
    // URL Yapısı: /api/categories/by-slug/gumus-kolye
    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug));
    }

    // 4. Ekleme (Admin)
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    // 5. Güncelleme (Update)
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }
}