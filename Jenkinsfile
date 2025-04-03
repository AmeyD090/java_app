pipeline {
    agent any

    environment {
        DOCKER_USER_NAME = 'yfhates'  // Your Docker Hub username
        DOCKER_REPO_NAME = 'banking-app'  // Your Docker repository name
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git branch: 'main', url: 'https://github.com/AmeyD090/java_app.git'
            }
        }
        stage('Verify Workspace') {
            steps {
                sh 'pwd'
                sh 'ls -R'
            }
        }
        stage('Build and Unit Testing') {
            steps {
                sh 'cd SimpleBanking && mvn clean package'
            }
        }
        stage('Verify Build Artifacts') {
            steps {
                sh 'ls -l SimpleBanking/target/'
            }
        }
        stage('Docker Image Build') {
            steps {
                sh 'docker build -t banking-app:latest -f SimpleBanking/Dockerfile SimpleBanking/'
            }
        }
        stage('Docker Image Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerHub',  // Use correct credential ID from Jenkins
                    usernameVariable: 'dockerHubUser',
                    passwordVariable: 'dockerHubPass'
                )]) {
                    sh 'echo $dockerHubPass | docker login -u $dockerHubUser --password-stdin'
                    sh 'docker tag banking-app:latest $DOCKER_USER_NAME/$DOCKER_REPO_NAME:latest'
                    sh 'docker push $DOCKER_USER_NAME/$DOCKER_REPO_NAME:latest'
                }
            }
        }
    }
}
