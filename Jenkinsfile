node {
  stage('Checkout'){
    git 'https://github.com/achudzicki/MyExpanse.git'
  }
  stage('Compile-Package') {
    sh 'mvn clean package'
  }
}
