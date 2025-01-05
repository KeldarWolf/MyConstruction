pipeline {
    agent any
    
    options {
        // Añadir opciones para permisos y timeout
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 1, unit: 'HOURS')
    }
    
    environment {
        PATH = "C:\\Program Files\\Git\\bin;${env.PATH}"
        NODEJS_HOME = tool 'NodeJS'
        PATH = "${env.NODEJS_HOME}\\npm;${env.PATH}"
        // Usar credentials binding para mayor seguridad
        ARTIFACTORY_CREDENTIALS = credentials('artifactory-credentials')
        GITHUB_CREDENTIALS = credentials('github-credentials')
        MAVEN_HOME = tool 'Maven'
        PATH = "${env.MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                withCredentials([usernamePassword(credentialsId: 'github-credentials', 
                                              usernameVariable: 'GIT_USERNAME', 
                                              passwordVariable: 'GIT_PASSWORD')]) {
                    git branch: 'main', 
                        url: 'https://github.com/KeldarWolf/MyConstruction.git',
                        credentialsId: 'github-credentials'
                }
            }
        }

        stage('Setup Permissions') {
            steps {
                script {
                    // Dar permisos de ejecución en Windows
                    bat 'icacls . /grant Everyone:(OI)(CI)F /T'
                    // Asegurar que los directorios tengan permisos correctos
                    bat 'mkdir -p myconstruction\\target || true'
                    bat 'mkdir -p myconstruction-front\\build || true'
                }
            }
        }

        stage('Build Backend') {
            steps {
                withMaven(maven: 'Maven') {
                    dir('myconstruction') {
                        bat 'mvn clean install -DskipTests'
                    }
                }
            }
        }

        stage('Test Backend') {
            steps {
                withMaven(maven: 'Maven') {
                    dir('myconstruction') {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                withEnv(["NODE_PATH=${tool 'NodeJS'}/bin"]) {
                    dir('myconstruction-front') {
                        bat 'npm install --legacy-peer-deps'
                        bat 'npm run build'
                    }
                }
            }
        }

        stage('Deploy to JFrog') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'artifactory-credentials',
                                              usernameVariable: 'ARTIFACTORY_USERNAME',
                                              passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
                    script {
                        def server = Artifactory.newServer(
                            url: 'http://localhost:8082/artifactory',
                            username: env.ARTIFACTORY_USERNAME,
                            password: env.ARTIFACTORY_PASSWORD
                        )
                        
                        def uploadSpec = """{
                            "files": [
                                {
                                    "pattern": "myconstruction/target/*.jar",
                                    "target": "my-construction-repo/backend/",
                                    "props": "type=jar;status=ready"
                                },
                                {
                                    "pattern": "myconstruction-front/build/**",
                                    "target": "my-construction-repo/frontend/",
                                    "props": "type=web;status=ready",
                                    "recursive": true,
                                    "flat": false
                                }
                            ]
                        }"""
                        
                        def buildInfo = server.upload spec: uploadSpec
                        buildInfo.env.collect()
                        server.publishBuildInfo buildInfo
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline ejecutado correctamente y artefactos subidos a JFrog'
            // Limpiar workspace preservando logs
            cleanWs(cleanWhenNotBuilt: false,
                   deleteDirs: true,
                   disableDeferredWipeout: true,
                   notFailBuild: true)
        }
        failure {
            echo 'El pipeline ha fallado'
            // Enviar notificación de fallo si es necesario
            emailext (
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'""",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
        always {
            cleanWs()
        }
    }
}
