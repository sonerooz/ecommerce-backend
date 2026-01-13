# 1. Adım: Java 21 sürümünü kullanıyoruz
FROM eclipse-temurin:21-jdk

# 2. Adım: Çalışma klasörünü ayarla
WORKDIR /app

# 3. Adım: Derlenen JAR dosyasını kopyala
COPY target/*.jar app.jar

# 4. Adım: Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]