package com.denizshopping.ecommerce.service;

import com.denizshopping.ecommerce.dto.ProductDto;
import com.denizshopping.ecommerce.entity.Product;
import com.denizshopping.ecommerce.mapper.ProductMapper;
import com.denizshopping.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Bunu ekle

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // OKUMA İşlemleri için readOnly = true (Performans artırır)
    // Bu sayede session, metod bitene kadar (Mapper dahil) açık kalır.
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productMapper.toDtoList(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByStore(Long storeId) {
        return productMapper.toDtoList(productRepository.findByStoreId(storeId));
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(product);
    }

    // YAZMA İşlemleri için normal Transactional
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);

        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> variant.setProduct(product));
        }
        if (product.getImages() != null) {
            product.getImages().forEach(image -> image.setProduct(product));
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
}