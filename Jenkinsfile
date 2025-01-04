pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Clonar el repositorio
                git branch: 'main', url: 'https://github.com/KeldarWolf/MyConstruction.git'
            }
        }

        stage('Build') {
            steps {
                // Navegar al directorio del proyecto y construirlo
                dir('myconstruction') {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Test') {
            steps {
                // Ejecutar pruebas si las hay
                dir('myconstruction') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        success {
            echo 'Build completado exitosamente.'
        }
        failure {
            echo 'Error durante el proceso de construcción.'
        }
    }
}
