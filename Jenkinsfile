pipeline {
    agent any

    stages {
        stage('Sistemi ve DB Hazırla') {
            steps {
                // 1. Ağ oluştur (Zaten varsa hata vermez)
                sh 'docker network create ecommerce-net || true'

                // 2. Genel DB (Catalog/Merchant için) başlat
                sh 'docker stop postgres-db || true'
                sh 'docker rm postgres-db || true'
                sh 'docker run -d --name postgres-db --network ecommerce-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=ecommerce_db postgres:15-alpine'

                // Auth DB, sunucuda docker-compose ile çalıştığı için ona dokunmuyoruz.
                // Sadece bağlantıların oturması için bekleme süresi
                sh 'sleep 10'
            }
        }

        stage('🚀 Gateway Deploy') {
            steps {
                dir('api-gateway') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t api-gateway .'
                    sh 'docker stop api-gateway-container || true'
                    sh 'docker rm api-gateway-container || true'

                    // Gateway: 8090
                    sh 'docker run -d --name api-gateway-container --network ecommerce-net -p 8090:8090 -e SERVER_PORT=8090 api-gateway'
                }
            }
        }

        stage('🛡️ Auth Deploy') {
            steps {
                dir('auth-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t auth-service .'
                    sh 'docker stop auth-service-container || true'
                    sh 'docker rm auth-service-container || true'

                    // DÜZELTME: Hibernate hatasını önlemek için Dialect ve Driver eklendi
                    sh '''
                        docker run -d --name auth-service-container \
                        --network ecommerce-net \
                        -p 8083:8083 \
                        -e SERVER_PORT=8083 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://auth-db:5432/ecommerce_auth \
                        -e SPRING_DATASOURCE_USERNAME=deniz \
                        -e SPRING_DATASOURCE_PASSWORD=12345 \
                        -e SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver \
                        -e SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        auth-service
                    '''
                }
            }
        }

        stage('📦 Catalog Deploy') {
            steps {
                dir('catalog-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t catalog-service .'
                    sh 'docker stop catalog-service-container || true'
                    sh 'docker rm catalog-service-container || true'

                    // Catalog: 8081
                    sh '''
                        docker run -d --name catalog-service-container \
                        --network ecommerce-net \
                        -p 8081:8081 \
                        -e SERVER_PORT=8081 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ecommerce_db \
                        -e SPRING_DATASOURCE_USERNAME=postgres \
                        -e SPRING_DATASOURCE_PASSWORD=password \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        catalog-service
                    '''
                }
            }
        }

        stage('🛒 Merchant Deploy') {
            steps {
                dir('merchant-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    sh 'docker build -t merchant-service .'
                    sh 'docker stop merchant-service-container || true'
                    sh 'docker rm merchant-service-container || true'

                    // Merchant: 8082
                    sh '''
                        docker run -d --name merchant-service-container \
                        --network ecommerce-net \
                        -p 8082:8082 \
                        -e SERVER_PORT=8082 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ecommerce_db \
                        -e SPRING_DATASOURCE_USERNAME=postgres \
                        -e SPRING_DATASOURCE_PASSWORD=password \
                        -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                        merchant-service
                    '''
                }
            }
        }
    }
}