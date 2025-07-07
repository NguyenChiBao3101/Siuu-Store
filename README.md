## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/NguyenChiBao3101/Siuu-Store.git
cd Siuu-Store/backend
```

### 2. Environment Configuration
Copy the example environment file and configure it:
```bash
cp .env.example .env
```

Edit `.env` file with your actual credentials:
- Database configuration
- reCAPTCHA keys
- Email credentials
- Google OAuth credentials
- RSA keys
- JASYPT 

### 3. Run the application
```bash
./mvnw spring-boot:run
```

## Environment Variables

The following environment variables need to be configured:

- `RECAPTCHA_SITE_KEY`: Google reCAPTCHA site key
- `RECAPTCHA_SECRET_KEY`: Google reCAPTCHA secret key
- `EMAIL_USERNAME`: Gmail username for sending emails
- `EMAIL_PASSWORD`: Gmail password or app password
- `GOOGLE_CLIENT_ID`: Google OAuth client ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth client secret
- `RSA_PUBLIC_KEY`: RSA public key for encryption
- `RSA_PRIVATE_KEY`: RSA private key for decryption

## Security Notes

- Never commit `.env` file or `application.properties` with real credentials
- Use environment variables for all sensitive information
- Keep your secrets secure and rotate them regularly
## Database 
Download db_backup.sql: https://drive.google.com/file/d/1npzIGLqG4PJ8hqmU6g2xibRBvOujlNtS/view?usp=sharing
run file with mySQL


