pipeline {
    agent any

    stages {
        stage('Sistemi ve DB Hazırla') {
            steps {
                sh 'docker network create ecommerce-net || true'

                // Veritabanını önce başlatıyoruz
                sh 'docker stop postgres-db || true'
                sh 'docker rm postgres-db || true'
                sh 'docker run -d --name postgres-db --network ecommerce-net -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=ecommerce_db postgres:15-alpine'

                // Veritabanının kendine gelmesi için 10 saniye bekleyelim
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
                    // Gateway'i de standart 8080'e zorluyoruz, dışarı 8090 veriyoruz
                    sh 'docker run -d --name api-gateway-container --network ecommerce-net -p 8090:8080 -e SERVER_PORT=8080 api-gateway'
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
                    // Auth Servisi: Dışarı 8081, İçeri 8080 (Zorunlu)
                    sh '''
                        docker run -d --name auth-service-container \
                        --network ecommerce-net \
                        -p 8081:8080 \
                        -e SERVER_PORT=8080 \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/ecommerce_db \
                        -e SPRING_DATASOURCE_USERNAME=postgres \
                        -e SPRING_DATASOURCE_PASSWORD=password \
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
                    // Catalog Servisi: Dışarı 8082, İçeri 8080 (Zorunlu)
                    sh '''
                        docker run -d --name catalog-service-container \
                        --network ecommerce-net \
                        -p 8082:8080 \
                        -e SERVER_PORT=8080 \
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
                    // Merchant Servisi: Dışarı 8083, İçeri 8080 (Zorunlu)
                    sh '''
                        docker run -d --name merchant-service-container \
                        --network ecommerce-net \
                        -p 8083:8080 \
                        -e SERVER_PORT=8080 \
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