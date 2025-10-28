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
                        # Create a new process environment with vars (ensures inheritance)
                        \$envVars = @{}
                        \$envVars['DB_URL'] = '${DB_URL}'
                        \$envVars['DB_PASSWORD'] = '${DB_PASSWORD}'
                        \$envVars['DB_USERNAME'] = '${DB_USERNAME}'
                        \$envVars['EMAIL_PASSWORD'] = '${EMAIL_PASSWORD}'
                        \$envVars['EMAIL_USERNAME'] = '${EMAIL_USERNAME}'
                        \$envVars['GOOGLE_CLIENT_ID'] = '${GOOGLE_CLIENT_ID}'
                        \$envVars['GOOGLE_CLIENT_SECRET'] = '${GOOGLE_CLIENT_SECRET}'
                        \$envVars['JASYPT_ENCRYPTOR_PASSWORD'] = '${JASYPT_ENCRYPTOR_PASSWORD}'
                        \$envVars['RECAPTCHA_SECRET_KEY'] = '${RECAPTCHA_SECRET_KEY}'
                        \$envVars['RECAPTCHA_SITE_KEY'] = '${RECAPTCHA_SITE_KEY}'
                        \$envVars['RSA_PRIVATE_KEY'] = @'
${RSA_PRIVATE_KEY}
'@
                        \$envVars['RSA_PUBLIC_KEY'] = '${RSA_PUBLIC_KEY}'

                        # Start with custom env
                        Start-Process -FilePath "java" -ArgumentList @('-jar', '${BACKEND_JAR}') -WindowStyle Hidden -EnvironmentVariables \$envVars
                    """
                }
                sleep time: 90, unit: 'SECONDS'  // Tăng thời gian chờ để app init đầy đủ (DB + JWT)
            }
        }

        stage('Check PORT') {
            steps {
                bat '''
                    echo === Checking if Backend API (port 8080) is running ===
                    netstat -ano | findstr :8080 >nul 2>&1
                    if %errorlevel% neq 0 (
                        echo Backend API not listening on port 8080!
                        echo Possible reasons: Startup failed (check console for output), port conflict, or config issue.
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
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do taskkill /PID %%a /F >nul 2>&1
                echo Process cleanup complete (no output if none found).
            '''
        }
        success {
            echo 'Pipeline completed successfully! App started on 8080 with default profile.'
        }
        failure {
            echo 'Pipeline failed - increase sleep time or check app config/env vars for startup issues.'
        }
    }
}