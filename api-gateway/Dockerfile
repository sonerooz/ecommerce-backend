# 1. Adım: Java 17 tabanlı hafif bir Linux sürümü kullan
FROM openjdk:17-jdk-slim

# 2. Adım: Çalışma klasörünü ayarla
WORKDIR /app

# 3. Adım: Derlenen JAR dosyasını kopyala ve adını 'app.jar' yap
# (Böylece versiyon değişse bile kodumuz bozulmaz)
COPY target/*.jar app.jar

# 4. Adım: Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]