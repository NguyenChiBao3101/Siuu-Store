pipeline {
    agent any
    environment {
        ZAP_HOME = 'C:\\Program Files\\ZAP\\Zed Attack Proxy'
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
                        Start-Process -FilePath "java" -ArgumentList @('-jar', '${BACKEND_JAR}') -WindowStyle Hidden
                    """
                }
                sleep time: 30, unit: 'SECONDS'
            }
        }
        stage('Start ZAP Proxy') {
            steps {
                dir("${env.ZAP_HOME}") {
                    bat '''
                        powershell -Command "Start-Process 'zap.bat' -ArgumentList '-daemon -port 8090 -config api.disablekey=true -config scripts.scriptsAutoLoad=true' -WindowStyle Hidden"
                    '''
                }
                sleep time: 20, unit: 'SECONDS'
            }
        }
        stage('Check PORT') {
            steps {
                bat '''
                    echo === Checking if ZAP (port 8090) is running ===
                    netstat -ano | findstr :8090 || echo ZAP proxy not listening on port 8090!
                    echo === Checking if Backend API (port 8080) is running ===
                    netstat -ano | findstr :8080 || echo Backend API not listening on port 8080!
                '''
            }
        }
        stage('Test Duplicate Signup Vulnerability') {
            steps {
                bat """
                    curl -x http://127.0.0.1:8090 ^
                         -X POST http://127.0.0.1:8080/api/v1/auth/register ^
                         -H "Content-Type: application/json" ^
                         -d "{\"first_name\": \"Test\",\"last_name\": \"User\",\"date_of_birth\": \"2003-01-31\",\"email\": \"test@duplicate.com\",\"phone_number\": \"0334067567\",\"password\": \"Pass123@\",\"confirm_password\": \"Pass123@\"}"
                """
                sleep time: 5, unit: 'SECONDS'
                bat """
                    curl -x http://127.0.0.1:8090 ^
                         -X POST http://127.0.0.1:8080/api/v1/auth/register ^
                         -H "Content-Type: application/json" ^
                         -d "{\"first_name\": \"Test2\",\"last_name\": \"User2\",\"date_of_birth\": \"2003-01-31\",\"email\": \"test@duplicate.com\",\"phone_number\": \"0334067568\",\"password\": \"Pass456@\",\"confirm_password\": \"Pass456@\"}"
                """
            }
        }
        stage('Append log vào workspace') {
            steps {
                bat '''
                    mkdir zap\\zap-reports 2>nul
                    copy /Y "${ZAP_HOME}\\zap-reports\\access.log" "zap\\zap-reports\\access.log" >nul 2>&1 || echo Log file may not exist yet, continuing...
                '''
            }
        }
        stage('Check Vulnerable') {
            steps {
                script {
                    def logPath = "zap/zap-reports/access.log"
                    if (!fileExists(logPath)) {
                        echo "Log file not found: ${logPath}. Proceeding without vulnerability check."
                    } else {
                        def content = readFile(logPath)
                        echo "Log:\n" + content
                        if (content.contains("*** VULNERABILITY DETECTED: Duplicate email signup allowed! ***")) {
                            error("Duplicate Signup Vulnerability detected in scan! Failing pipeline.")
                        } else {
                            echo "No Duplicate Signup vulnerabilities detected in latest scan."
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            bat '''
                echo === Stopping backend process ===
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do taskkill /PID %%a /F >nul 2>&1
                echo === Stopping ZAP process ===
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8090') do taskkill /PID %%a /F >nul 2>&1
                echo Process cleanup complete.
            '''
            publishHTML(target: [
                reportDir: "zap\\zap-reports",
                reportFiles: "access.log",
                reportName: 'ZAP Duplicate Signup Report',
                keepAll: true
            ])
        }
        success {
            echo 'Pipeline completed successfully! App started on 8080 with default profile. No vulnerabilities detected.'
        }
        failure {
            echo 'Pipeline failed - Check vulnerability detection, increase sleep time, or verify app config/env vars.'
        }
    }
}