package com.siuuuuu.backend.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RsaBcryptPasswordEncoder implements PasswordEncoder {
    private RSAPrivateKey privateKey;
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    public RsaBcryptPasswordEncoder(String privateKeyBase64) throws Exception {
        // Khởi tạo BCryptPasswordEncoder
        this.bcryptPasswordEncoder = new BCryptPasswordEncoder();

        // Tải khóa riêng từ base64
        this.privateKey = loadPrivateKey(privateKeyBase64);
    }

    // Tải khóa riêng từ chuỗi base64
    private RSAPrivateKey loadPrivateKey(String keyBase64) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(decodedKey));
    }

    // Giải mã mật khẩu RSA
    private String decrypt(String encryptedPassword) throws Exception {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("RSA Decryption failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Mã hóa lại mật khẩu bằng bcrypt
        return bcryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Kiểm tra xem mật khẩu sau khi giải mã có khớp với bcrypt đã mã hóa hay không
        try {
            String decryptedPassword = decrypt(rawPassword.toString());
            return bcryptPasswordEncoder.matches(decryptedPassword, encodedPassword);
        } catch (Exception e) {
            System.err.println("Password matching failed: " + e.getMessage());
            return false;
        }
    }
}
