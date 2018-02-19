pipeline {
    agent any
    triggers { pollSCM('H/2 * * * *') }
    stages {
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
                sh 'gradle war -b build_jenkins.gradle'
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
                                    sh 'gradle test -i -b build_jenkins.gradle'
                                }
                            }
                            catch (exc) {
                                sh "docker logs ${backend.id}"
                                throw exc
                            }
                        }
                    }
                }
                step([$class: 'JUnitResultArchiver', testResults: '**/testResults/*.xml'])
            }
        }
        stage('analyze code convention') {
            agent {
                docker { 
                    image 'gradle:latest'
                    reuseNode true 
                }
            }
            steps {
                script {
                    sh "gradle sonarqube -Dsonar.organization=munhunger-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=26f034126050250c874ef2220dc47ef9245c0710 -Dsonar.branch.name=${env.BRANCH_NAME}"
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
        }
        success {
            slackSend(color: '#0F0', message: "Build success: ${env.JOB_NAME} #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")
        }
    }
}
