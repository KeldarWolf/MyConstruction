pipeline {
    agent any

    environment {
        PATH = "C:\\Program Files\\Git\\bin;${env.PATH}" // Asegúrate de que Git esté en el PATH
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/KeldarWolf/MyConstruction.git'
            }
        }
        stage('Build') {
            steps {
                dir('myconstruction') {
                    bat 'mvn clean install' // Usa 'bat' en lugar de 'sh' para sistemas Windows
                }
            }
        }
        stage('Test') {
            steps {
                dir('myconstruction') {
                    bat 'mvn test' // Ejecución de pruebas
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado exitosamente.'
        }
        failure {
            echo 'Error durante el proceso de construcción.'
        }
    }
}
