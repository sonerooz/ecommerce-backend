pipeline {
    agent any

    environment {
        // Ortak Ağ İsmi
        NETWORK_NAME = "ecommerce-net"
        // Veritabanı Bilgileri
        DB_CONTAINER = "postgres-db"
        DB_USER      = "postgres"
        DB_PASS      = "password"
        DB_NAME      = "ecommerce_db"
        // Resimlerin Saklanacağı Host Dizini (Jenkins sunucusundaki fiziksel yol)
        HOST_UPLOAD_PATH = "/home/jenkins/uploads_data"
    }

    stages {
        stage('🛠️ Altyapı Hazırlığı') {
            steps {
                // 1. Docker ağını oluştur (Zaten varsa hata vermez)
                sh "docker network create ${NETWORK_NAME} || true"

                // 2. Genel PostgreSQL konteynerini başlat (Catalog & Merchant için)
                sh "docker stop ${DB_CONTAINER} || true"
                sh "docker rm ${DB_CONTAINER} || true"
                sh "docker run -d --name ${DB_CONTAINER} --network ${NETWORK_NAME} -e POSTGRES_USER=${DB_USER} -e POSTGRES_PASSWORD=${DB_PASS} -e POSTGRES_DB=${DB_NAME} postgres:15-alpine"

                // DB'nin hazır olması için bekleme
                sh 'sleep 10'
            }
        }

        stage('📸 Storage Service Deploy') {
            steps {
                dir('storage-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t storage-service .'
                    sh 'docker stop storage-service-container || true'
                    sh 'docker rm storage-service-container || true'

                    // Önemli: Resimlerin silinmemesi için volume mapping eklendi
                    sh """
                        docker run -d --name storage-service-container \
                        --network ${NETWORK_NAME} \
                        -p 8084:8084 \
                        -v ${HOST_UPLOAD_PATH}:/app/uploads \
                        -e SERVER_PORT=8084 \
                        -e FILE_UPLOAD_DIR=/app/uploads \
                        storage-service
                    """
                }
            }
        }

        stage('🚀 API Gateway Deploy') {
            steps {
                dir('api-gateway') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t api-gateway .'
                    sh 'docker stop api-gateway-container || true'
                    sh 'docker rm api-gateway-container || true'

                    // Canlı ortamda servislerin birbirini ağ üzerinden bulması için URL'ler tanımlandı
                    sh """
                        docker run -d --name api-gateway-container \
                        --network ${NETWORK_NAME} \
                        -p 8090:8090 \
                        -e SERVER_PORT=8090 \
                        -e AUTH_SERVICE_URL=http://auth-service-container:8083 \
                        -e CATALOG_SERVICE_URL=http://catalog-service-container:8081 \
                        -e MERCHANT_SERVICE_URL=http://merchant-service-container:8082 \
                        -e STORAGE_SERVICE_URL=http://storage-service-container:8084 \
                        api-gateway
                    """
                }
            }
        }

        stage('🛡️ Auth Service Deploy') {
            steps {
                dir('auth-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t auth-service .'
                    sh 'docker stop auth-service-container || true'
                    sh 'docker rm auth-service-container || true'

                    sh """
                        docker run -d --name auth-service-container \
                        --network ${NETWORK_NAME} \
                        -p 8083:8083 \
                        -e SERVER_PORT=8083 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://auth-db:5432/ecommerce_auth \
                        -e SPRING_DATASOURCE_USERNAME=deniz \
                        -e SPRING_DATASOURCE_PASSWORD=12345 \
                        auth-service
                    """
                }
            }
        }

        stage('📦 Catalog Service Deploy') {
            steps {
                dir('catalog-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t catalog-service .'
                    sh 'docker stop catalog-service-container || true'
                    sh 'docker rm catalog-service-container || true'

                    sh """
                        docker run -d --name catalog-service-container \
                        --network ${NETWORK_NAME} \
                        -p 8081:8081 \
                        -e SERVER_PORT=8081 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ecommerce_db \
                        -e SPRING_DATASOURCE_USERNAME=${DB_USER} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_PASS} \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        catalog-service
                    """
                }
            }
        }

        stage('🛒 Merchant Service Deploy') {
            steps {
                dir('merchant-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t merchant-service .'
                    sh 'docker stop merchant-service-container || true'
                    sh 'docker rm merchant-service-container || true'

                    sh """
                        docker run -d --name merchant-service-container \
                        --network ${NETWORK_NAME} \
                        -p 8082:8082 \
                        -e SERVER_PORT=8082 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ecommerce_db \
                        -e SPRING_DATASOURCE_USERNAME=${DB_USER} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_PASS} \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        merchant-service
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Tüm servisler başarıyla ayağa kalktı. Sistem 8090 portu üzerinden yayında.'
        }
        failure {
            echo '❌ Dağıtım başarısız oldu. Lütfen Jenkins loglarını inceleyin.'
        }
    }
}