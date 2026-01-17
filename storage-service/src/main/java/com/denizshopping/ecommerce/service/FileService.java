package com.denizshopping.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootLocation;

    public FileService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Klasör oluşturulamadı!", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) throw new RuntimeException("Dosya boş!");

            // Uzantıyı al (jpg, png vb.)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Benzersiz isim oluştur
            String filename = UUID.randomUUID().toString() + extension;

            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));

            // Frontend'e dönecek URL (Gateway portun 8090 üzerinden)
            return "http://localhost:8090/api/storage/files/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Dosya saklanamadı!", e);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Dosya okunamıyor!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Dosya yolu hatası!", e);
        }
    }
}