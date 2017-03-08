node {
   def mvnHome
   stage('Preparation') { 
      // Get the source code
      git 'https://github.com/CSC-IT-Center-for-Science/lega-config-microservice.git'

      // Init Java and Maven              
      mvnHome = tool 'M3'
      env.JAVA_HOME = tool 'JDK 8'
   }
   stage('Unit test') {
       // Run unit tests
       sh "ls -lh"
       sh "pwd"
       sh "export"	
       sh "'${mvnHome}/bin/mvn' -DargLine='-Dspring.profiles.active=dev' clean test"
   }
   stage('Build') {
      // Run the maven build
      sh "'${mvnHome}/bin/mvn' -Dmaven.test.skip=true -DargLine='-Dspring.profiles.active=dev' clean package"

   }
//   stage('Results') {
//      junit 'target/surefire-reports/TEST-*.xml'
//      archive 'target/*.jar'
//   }
}
