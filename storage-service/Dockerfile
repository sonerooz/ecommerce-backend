# 1. Adım: Java 21 sürümünü kullanıyoruz
FROM eclipse-temurin:21-jdk

# 2. Adım: Çalışma klasörünü ayarla
WORKDIR /app

# 3. Adım: Yüklenen dosyalar için klasör oluştur (ÇOK KRİTİK)
# Uygulama ayağa kalktığında bu klasöre yazma yetkisi hazır olmalı.
RUN mkdir -p /app/uploads

# 4. Adım: Derlenen JAR dosyasını kopyala
COPY target/*.jar app.jar

# 5. Adım: Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]