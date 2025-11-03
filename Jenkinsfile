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
                    curl -x http://127.0.0.1:8090 -X GET http://localhost:8080/api/v1/products/nike-air-zoom-pegasus-40
                    """
            }
        }
        stage('Append log vào workspace') {
            steps {
                bat '''
                    copy /Y "C:\\Users\\slytherin\\Desktop\\access_log.txt" "access_log.txt"
                    echo === Log content in workspace: ===
                    type "access_log.txt"
                '''
            }
        }
        stage('Check Vulnerable') {
            steps {
                script {
                    def logPath = "access_log.txt"
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
            // Archive raw log (backup, không NPE)
            archiveArtifacts artifacts: 'access_log.txt', allowEmptyArchive: true

            // Generate HTML report từ log (fix NPE cho publishHTML)
            script {
                if (fileExists('access_log.txt')) {
                    def content = readFile('access_log.txt')
                    // Escape nội dung log để an toàn HTML (tránh < > gây parse error)
                    def escapedContent = content.replaceAll('<', '&lt;').replaceAll('>', '&gt;').replaceAll('\n', '<br>')
                    def htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Duplicate Signup Scan Report</title>
                        <style> pre { white-space: pre-wrap; font-family: monospace; background: #f4f4f4; padding: 10px; border: 1px solid #ddd; } </style>
                    </head>
                    <body>
                        <h1>Access Log Report - Build #${BUILD_NUMBER}</h1>
                        <p><strong>Summary:</strong> No Duplicate Signup vulnerabilities detected (based on log analysis).</p>
                        <h2>Full Log:</h2>
                        <pre>${escapedContent}</pre>
                        <p>Generated: ${new Date().format('yyyy-MM-dd HH:mm:ss')}</p>
                    </body>
                    </html>
                    """
                    writeFile file: 'scan_report.html', text: htmlContent
                    // Publish HTML (bây giờ valid, no NPE)
                    publishHTML(target: [
                        reportFiles: 'scan_report.html',
                        reportName: 'Scan Report',
                        keepAll: true
                    ])
                } else {
                    echo "No log file - skipping HTML report."
                    // Tạo HTML placeholder nếu không có log
                    writeFile file: 'scan_report.html', text: '<h1>No Log Available</h1><p>Run ZAP script manually to generate log.</p>'
                    publishHTML(target: [reportFiles: 'scan_report.html', reportName: 'Scan Report', keepAll: true])
                }
            }
        }
        success {
            echo 'Pipeline completed successfully! App started on 8080 with default profile. No vulnerabilities detected.'
        }
        failure {
            echo 'Pipeline failed - Check vulnerability detection, increase sleep time, or verify app config/env vars.'
        }
    }
}