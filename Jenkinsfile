pipeline {
    agent any

    stages {
        stage('Gateway Build') {
            steps {
                dir('api-gateway') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        stage('Auth Build') {
            steps {
                dir('auth-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        stage('Catalog Build') {
            steps {
                dir('catalog-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        stage('Merchant Build') {
            steps {
                dir('merchant-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
    }
}