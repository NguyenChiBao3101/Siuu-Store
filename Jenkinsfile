pipeline {
    agent any

    environment {
        BACKEND_JAR = 'target\\siuushop-0.0.1-SNAPSHOT.jar'
        BACKEND_LOG = 'backend.log'
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

                    // RSA Keys (multi-line OK với Secret text)
                    string(credentialsId: 'RSA_PRIVATE_KEY', variable: 'RSA_PRIVATE_KEY'),
                    string(credentialsId: 'RSA_PUBLIC_KEY', variable: 'RSA_PUBLIC_KEY')
                ]) {
                    powershell """
                        # Set env vars explicit (Jenkins injects them, but confirm)
                        \$env:DB_URL = '${DB_URL}'
                        \$env:DB_PASSWORD = '${DB_PASSWORD}'
                        \$env:DB_USERNAME = '${DB_USERNAME}'
                        \$env:EMAIL_PASSWORD = '${EMAIL_PASSWORD}'
                        \$env:EMAIL_USERNAME = '${EMAIL_USERNAME}'
                        \$env:GOOGLE_CLIENT_ID = '${GOOGLE_CLIENT_ID}'
                        \$env:GOOGLE_CLIENT_SECRET = '${GOOGLE_CLIENT_SECRET}'
                        \$env:JASYPT_ENCRYPTOR_PASSWORD = '${JASYPT_ENCRYPTOR_PASSWORD}'
                        \$env:RECAPTCHA_SECRET_KEY = '${RECAPTCHA_SECRET_KEY}'
                        \$env:RECAPTCHA_SITE_KEY = '${RECAPTCHA_SITE_KEY}'
                        \$env:RSA_PRIVATE_KEY = '${RSA_PRIVATE_KEY}'
                        \$env:RSA_PUBLIC_KEY = '${RSA_PUBLIC_KEY}'

                        # Start app với profile local (nếu cần)
                        Start-Process -FilePath "java" -ArgumentList @('-jar', '${BACKEND_JAR}') -WindowStyle Hidden -RedirectStandardOutput '${BACKEND_LOG}' -RedirectStandardError '${BACKEND_LOG}'
                    """
                }
                sleep time: 60, unit: 'SECONDS'  // Chờ startup (JWT + DB init)
            }
        }

        stage('Check PORT') {
            steps {
                bat '''
                    echo === Checking if Backend API (port 8080) is running ===
                    netstat -ano | findstr :8080 > nul
                    if errorlevel 1 (
                        echo Backend API not listening on port 8080!
                        echo === Backend startup logs ===
                        type backend.log
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
                    if errorlevel 1 (
                        echo Failed to kill PID %%a (may not exist)
                    ) else (
                        echo Killed PID %%a
                    )
                )
                echo === Backend logs (last 50 lines) ===
                if exist backend.log (
                    powershell "Get-Content backend.log -Tail 50"
                ) else (
                    echo No log file found
                )
            '''
        }
        success {
            echo 'Pipeline completed successfully! App started on 8080.'
        }
        failure {
            echo 'Pipeline failed - check backend.log for details (e.g., DB connection).'
        }
    }
}