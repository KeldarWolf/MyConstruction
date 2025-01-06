pipeline {
    agent any

    environment {
        PATH = "C:\\Program Files\\Git\\bin;${env.PATH}" // Asegúrate de que Git esté en el PATH
        ARTIFACTORY_URL = 'https://tu-artifactory-url' // Reemplaza con la URL de tu Artifactory
        ARTIFACTORY_CREDENTIALS = 'Artifactory-credentials-id' // Reemplaza con el ID de las credenciales en Jenkins
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

        stage('Publish to Artifactory') {
            steps {
                script {
                    // Publica artefactos a Artifactory utilizando las credenciales
                    rtMavenDeployer(
                        serverId: 'Artifactory',  // ID del servidor configurado en Jenkins
                        deployerRepo: 'libs-release-local',  // Repositorio en Artifactory donde se sube el artefacto
                        credentialsId: ARTIFACTORY_CREDENTIALS,  // Credenciales de Jenkins
                        artifact: 'target/*.jar'  // Ubicación del artefacto generado
                    )
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
