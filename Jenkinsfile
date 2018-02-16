pipeline {
    agent any
    stages {
        stage('github pending status') {
            steps {
                script {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '86bc4b4c-e630-4238-b9f4-22270d1077b0',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'echo uname=$USERNAME pwd=$PASSWORD'
                        sh 'curl "https://api.github.com/repos/munhunger/idp/statuses/$GIT_COMMIT?access_token=$PASSWORD" \
                                -H "Content-Type: application/json" \
                                -X POST \
                                -d "{\"state\": \"pending\", \"description\": \"Jenkins\", \"target_url\": \"http://my.jenkins.box.com/job/dividata/$BUILD_NUMBER/console\"}"'
                    }
                }
            }
        }
        stage ('Clean') {
            steps {
                deleteDir()
            }
        }
        stage ('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('build war') {
            agent {
                docker { 
                    image 'gradle:latest'
                    reuseNode true 
                }
            }
            steps {
                sh 'gradle war'
                sh 'ls build'
            }
        }
        stage('test') {
            steps {
                script {
                    def image = docker.build("munhunger/idp");
                    docker.image('mysql:latest').withRun('-e "MYSQL_ROOT_PASSWORD=password" -e "MYSQL_USER=root" -e "MYSQL_DATABASE=idp"') { mysql -> 
                        image.withRun("--link ${mysql.id}:db -e DB_URL=mysql://db:3306/idp?useSSL=false -e DB_PASS=password -e DB_USER=root") { backend -> 

                            docker.image('mysql:latest').inside("--link ${mysql.id}:db") {
                                sh 'while ! mysqladmin ping -hdb --silent; do sleep 1; done'
                            }
                            image.inside("--link ${mysql.id}:db") {
                                sh 'sleep 5'
                            }
                            try {
                                docker.image('gradle:latest').inside("--link ${backend.id}:backend -e 'IDP_URL=http://backend:8080'") {
                                    sh 'gradle test -i'
                                }
                            }
                            catch (exc) {
                                sh "docker logs ${backend.id}"
                                throw exc
                            }
                        }
                    }
                }
            }
        }
        stage('build dockerimage') {
            steps {
                script {
                    def image = docker.build("munhunger/idp")
                    
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        image.push("${env.BUILD_NUMBER}")
                        image.push("latest")
                    }
                }
            }
        }
    }
    post {
        failure {
            slackSend(color: '#F00', message: "Build failure: ${env.JOB_NAME} #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: '86bc4b4c-e630-4238-b9f4-22270d1077b0',
            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                sh 'echo uname=$USERNAME pwd=$PASSWORD'
                sh 'curl "https://api.github.com/repos/munhunger/idp/statuses/$GIT_COMMIT?access_token=$PASSWORD" \
                        -H "Content-Type: application/json" \
                        -X POST \
                        -d "{\"state\": \"failure\", \"description\": \"Jenkins\", \"target_url\": \"http://my.jenkins.box.com/job/dividata/$BUILD_NUMBER/console\"}"'
            }
        }
        success {
            slackSend(color: '#0F0', message: "Build success: ${env.JOB_NAME} #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")
                sh 'curl "https://api.github.com/repos/munhunger/idp/statuses/$GIT_COMMIT?access_token=$PASSWORD" \
                        -H "Content-Type: application/json" \
                        -X POST \
                        -d "{\"state\": \"success\", \"description\": \"Jenkins\", \"target_url\": \"http://my.jenkins.box.com/job/dividata/$BUILD_NUMBER/console\"}"'
        }
    }
}
