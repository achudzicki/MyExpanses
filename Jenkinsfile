node {
  stage('Checkout'){
    git 'https://github.com/achudzicki/MyExpanse'
  }
  stage('Compile-Package') {
    sh 'mvn clean package'
  }
}
