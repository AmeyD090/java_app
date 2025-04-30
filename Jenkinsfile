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
         stage('Sonarqube Analysis') {
             enviroment{
                  SCANNER_HOME=tool 'sonar-scanner'
             }
            steps {
                    withSonarQubeEnv('sonar-server') {
                        sh ''' $SCANNER_HOME/bin/sonar-scanner \
                        -Dsonar.projectName=java_app \
                        -Dsonar.projectKey=java_app '''
                    }
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
        stage('Update Deployment file'){
            enviroments{
                GIT_REPO = 'https://github.com/AmeyD090/java_app.git'
                GIT_USER_NAME = 'AmeyD090'
            }
            withCredentials([usrnamePassword(credentialsId:'github' , usernameVariable:'GITHUB_USER' , passwordvariable:'GITHUB_TOKEN')]){
                sh '''
                git config user.email "ameydeshmukh090@gmail.com"
                git config user.name "AmeyD090"


                BUILD_NUMBER=${BUILD_NUMBER}
                echo "Current BUILD_NUMBER ${BUILD_NUMBER}"

                 # Extract the current tag from values.yaml
                  imageTag=$(grep -oP "(?<=banking-app:\\s)[^\\n]+" deployment.yml)
                  echo "Current image tag: $imageTag"
                  # Replace the tag value with the build number
                  sed -i "s/tag: ${imageTag}/banking-app: ${BUILD_NUMBER}/" deployment.yaml
                  # Commit and push the changes
                  git add values.yml

                  git commit -m "Updates tag value ${BUILD_NUMBER}"
                  git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/java_app.git HEAD:main
                 
            }
        }
    }
}
