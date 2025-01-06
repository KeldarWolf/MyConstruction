pipeline {
    agent any

    environment {
        PATH = "C:\\Program Files\\Git\\bin;${env.PATH}" // Asegúrate de que Git esté en el PATH
        ARTIFACTORY_URL = 'https://tu-artifactory-url' // Reemplaza con la URL de tu Artifactory
        ARTIFACTORY_USERNAME = 'admin' // Usuario de Artifactory
        ARTIFACTORY_PASSWORD = 'password' // Contraseña de Artifactory
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
                    // Publica artefactos a Artifactory utilizando las credenciales de usuario y contraseña
                    def server = Artifactory.server('Artifactory')  // Nombre del servidor configurado en Jenkins
                    def rtRepo = Artifactory.mavenRepo('libs-release-local')  // Repositorio donde se sube el artefacto

                    // Publicar artefacto
                    server.upload(
                        spec: """{
                            "files": [
                                {
                                    "pattern": "target/*.jar", 
                                    "target": "libs-release-local/myconstruction/"
                                }
                            ]
                        }"""
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

