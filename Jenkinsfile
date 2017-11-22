pipeline {
  agent any
  stages {
    stage('Clean') {
      steps {
        sh '''cd /opt/dev/projects/github/wedeploy-ci/app

gradle clean
'''
      }
    }
    stage('Compile') {
      steps {
        sh '''cd /opt/dev/projects/github/wedeploy-ci/app

gradle compileJava
'''
      }
    }
  }
}