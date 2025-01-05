pipeline {
    agent any
    
    environment {
        PATH = "C:\\Program Files\\Git\\bin;${env.PATH}"
        NODEJS_HOME = tool 'NodeJS'
        PATH = "${env.NODEJS_HOME}\\npm;${env.PATH}"
        JFROG_CREDENTIALS = credentials('jfrog-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git branch: 'main', url: 'https://github.com/KeldarWolf/MyConstruction.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('myconstruction') {
                    bat 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('myconstruction') {
                    bat 'mvn test'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('myconstruction-front') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Deploy to JFrog') {
            steps {
                script {
                    // Configuración para subir artefactos a JFrog
                    def server = Artifactory.newServer url: 'http://localhost:8082/artifactory', 
                                                    username: 'admin', 
                                                    password: 'Kevin023'
                    
                    // Subir el archivo JAR del backend
                    def uploadSpec = """{
                        "files": [{
                            "pattern": "myconstruction/target/*.jar",
                            "target": "my-construction-repo/backend/"
                        },
                        {
                            "pattern": "myconstruction-front/build/**",
                            "target": "my-construction-repo/frontend/"
                        }]
                    }"""
                    
                    def buildInfo = server.upload spec: uploadSpec
                    server.publishBuildInfo buildInfo
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline ejecutado correctamente y artefactos subidos a JFrog'
        }
        failure {
            echo 'El pipeline ha fallado'
        }
        always {
            cleanWs()
        }
    }
}
