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
                withCredentials([
                    // Database
                    string(credentialsId: 'DB_URL', variable: 'DB_URL'),
                    string(credentialsId: 'DB_PASSWORD', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'DB_USERNAME', variable: 'DB_USERNAME'),
                    
                    // Email
                    string(credentialsId: 'EMAIL_PASSWORD', variable: 'EMAIL_PASSWORD'),
                    string(credentialsId: 'EMAIL_USERNAME', variable: 'EMAIL_USERNAME'),
                    
                    // Google OAuth
                    string(credentialsId: 'GOOGLE_CLIENT_ID', variable: 'GOOGLE_CLIENT_ID'),
                    string(credentialsId: 'GOOGLE_CLIENT_SECRET', variable: 'GOOGLE_CLIENT_SECRET'),
                    
                    // Jasypt
                    string(credentialsId: 'JASYPT_ENCRYPTOR_PASSWORD', variable: 'JASYPT_ENCRYPTOR_PASSWORD'),
                    
                    // reCAPTCHA
                    string(credentialsId: 'RECAPTCHA_SECRET_KEY', variable: 'RECAPTCHA_SECRET_KEY'),
                    string(credentialsId: 'RECAPTCHA_SITE_KEY', variable: 'RECAPTCHA_SITE_KEY'),
                    
                    // RSA Keys
                    string(credentialsId: 'RSA_PRIVATE_KEY', variable: 'RSA_PRIVATE_KEY'),
                    string(credentialsId: 'RSA_PUBLIC_KEY', variable: 'RSA_PUBLIC_KEY')
                ]) {
                    powershell """
                        # Jenkins env vars are auto-injected
                        
                        # Start app without redirect (output to console)
                        Start-Process -FilePath "java" -ArgumentList @('-jar', '${BACKEND_JAR}') -WindowStyle Hidden
                    """
                }
                sleep time: 60, unit: 'SECONDS'
            }
        }

        stage('Check PORT') {
            steps {
                bat '''
                    echo === Checking if Backend API (port 8080) is running ===
                    netstat -ano | findstr :8080 >nul 2>&1
                    if %errorlevel% neq 0 (
                        echo Backend API not listening on port 8080!
                        exit /b 1
                    ) else (
                        echo Backend API is listening on port 8080 - SUCCESS!
                    )
                '''
            }
        }
    }

    post {
        always {
            bat '''
                echo === Stopping backend process ===
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
                    taskkill /PID %%a /F >nul 2>&1
                    if !errorlevel! neq 0 (
                        echo Failed to kill PID %%a (may not exist)
                    ) else (
                        echo Killed PID %%a
                    )
                )
            '''
        }
        success {
            echo 'Pipeline completed successfully! App started on 8080 with default profile.'
        }
        failure {
            echo 'Pipeline failed - check console for app output (no log file).'
        }
    }
}