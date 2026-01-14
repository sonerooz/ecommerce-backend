pipeline {
    agent any

    stages {
        stage('Sistemi Hazırla') {
            steps {
                sh 'docker network create ecommerce-net || true'
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
                    sh 'docker run -d --name api-gateway-container --network ecommerce-net -p 8090:8080 api-gateway'
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
                    // Auth: 8081 (Dışarı açtık)
                    sh 'docker run -d --name auth-service-container --network ecommerce-net -p 8081:8080 auth-service'
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
                    // Catalog: 8082 (Dışarı açtık - Swagger burada!)
                    sh 'docker run -d --name catalog-service-container --network ecommerce-net -p 8082:8080 catalog-service'
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
                    // Merchant: 8083 (Dışarı açtık)
                    sh 'docker run -d --name merchant-service-container --network ecommerce-net -p 8083:8080 merchant-service'
                }
            }
        }
    }
}