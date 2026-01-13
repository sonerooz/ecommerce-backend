pipeline {
    agent any

    stages {
        stage('Sistemi Hazırla') {
            steps {
                // Konteynırların birbiriyle konuşması için özel bir ağ kuruyoruz
                sh 'docker network create ecommerce-net || true'
            }
        }

        stage('🚀 Gateway Deploy') {
            steps {
                dir('api-gateway') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                    // Docker imajını oluştur
                    sh 'docker build -t api-gateway .'
                    // Eski konteynır varsa durdur ve sil
                    sh 'docker stop api-gateway-container || true'
                    sh 'docker rm api-gateway-container || true'
                    // Yenisini başlat (DIKKAT: 80 portunu 8080'e bağlıyoruz)
                    sh 'docker run -d --name api-gateway-container --network ecommerce-net -p 80:8080 api-gateway'
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
                    // Auth servisini ağa dahil et
                    sh 'docker run -d --name auth-service-container --network ecommerce-net auth-service'
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
                    sh 'docker run -d --name catalog-service-container --network ecommerce-net catalog-service'
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
                    sh 'docker run -d --name merchant-service-container --network ecommerce-net merchant-service'
                }
            }
        }
    }
}