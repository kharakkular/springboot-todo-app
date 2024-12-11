pipeline {
	agent any 
	environment {
		MAVEN_HOME = '/opt/maven'
	}
	stages {
		stage('Checkout Code') {
			steps {
				script {
					git branch: 'main',
						credentialsId: 'kharak-git-credential',
						url: 'https://github.com/kharakkular/springboot-todo-app.git'
				} 
			}
		}
		stage('Build') {
			steps {
				sh '${MAVEN_HOME}/bin/mvn clean package'
			}
		}
		stage('Deploy') {
			steps {
				sh 'cp target/todo-rest-api-0.0.1-SNAPSHOT.war /opt/tomcat/webapps/'
				sh '/opt/tomcat/bin/shutdown.sh || true'
				sh '/opt/tomcat/bin//startup.sh'
			}
		}
	}
	post {
		always {
			echo 'Pipeline Complete'
		}
	}
}
