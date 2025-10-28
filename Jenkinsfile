pipeline {
    agent any

    environment {
        BACKEND_JAR = 'target\\siuushop-0.0.1-SNAPSHOT.jar'
    }

    stages {
        stage('Build Project') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Start Backend API') {
            steps {
                powershell """
                    Start-Process -FilePath "java" -ArgumentList @('-jar', '${BACKEND_JAR}') -WindowStyle Hidden
                """
                sleep time: 25, unit: 'SECONDS'
            }
        }

        stage('Check PORT') {
            steps {
                bat '''
                    netstat -ano
                '''
            }
        }
    }

    post {
        always {
            // Optional: Stop the backend process
            bat '''
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do taskkill /PID %%a /F >nul 2>&1 || echo No process to kill
            '''
        }
    }
}