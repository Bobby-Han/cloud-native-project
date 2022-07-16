pipeline {
    agent none
    stages {
        stage('Clone to master') {
            agent {
                label 'master'

            }
            steps {
                echo "1. Git Clone Stage"
                git url: "https://github.com/Bobby-Han/cloud-native-project.git"

            }

        }
        stage('Maven Build') {
            agent {
                docker {
                    image 'maven:latest'
                    args '-v /root/.m2:/root/.m2'

                }

            }
            steps {
                echo "2. Maven Build Stage"
                sh 'mvn -B clean package -Dmaven.test.skip=true'

            }

        }
        stage('Image Build') {
            agent {
                label 'master'

            }
            steps {
                echo "3. Image Build Stage"
                sh 'docker build -f Dockerfile --build-arg jar_name=target/CloudNativeProject-0.0.1-SNAPSHOT.jar -t cloud-native-project:${BUILD_ID} . '
                sh 'docker tag cloud-native-project:${BUILD_ID} harbor.edu.cn/nju15/cloud-native-project:${BUILD_ID}'

            }

        }
        stage('Push') {
            agent {
                label 'master'

            }
            steps {
                echo "4. Push Docker Image Stage"
                sh "docker login --username=nju15 harbor.edu.cn -p nju152022"
                sh "docker push harbor.edu.cn/nju15/cloud-native-project:${BUILD_ID}"

            }

        }

    }

}

node('slave') {
    container('jnlp-kubectl') {
        stage('Git Clone') {
            git url: "https://github.com/Bobby-Han/cloud-native-project.git"
        }
        stage('YAML') {
            echo "5. Change YAML File Stage"
            sh 'sed -i "s#{VERSION}#${BUILD_ID}#g" ./jenkins/scripts/cloud-native-project.yaml'

        }
        stage('Deploy') {
            echo "6. Deploy To K8s Stage"
            sh 'kubectl apply -f ./jenkins/scripts/cloud-native-project.yaml -n nju15'
            sh 'kubectl apply -f ./jenkins/scripts/cloud-native-project-serviceMonitor.yaml'

        }
    }

}