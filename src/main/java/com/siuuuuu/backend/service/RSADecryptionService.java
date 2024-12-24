package com.siuuuuu.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

@Service
public class RSADecryptionService {

    // Khóa riêng tư được lưu trữ dưới dạng Base64 (lấy từ cấu hình)
    @Value("${spring.security.rsa.privatekey}")
    private String PRIVATE_KEY;

    private RSAPrivateKey privateKey;

    // Tải khóa riêng tư từ chuỗi Base64 (khởi tạo khi service được gọi)
    private RSAPrivateKey loadPrivateKey() throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(PRIVATE_KEY);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

    // Giải mã dữ liệu mã hóa RSA
    public String decrypt(String encryptedData) {
        try {
            // Tải khóa riêng tư từ Base64 (nếu chưa tải)
            if (privateKey == null) {
                privateKey = loadPrivateKey();
            }

            // Khởi tạo Cipher với chế độ giải mã
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Giải mã dữ liệu
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Chuyển kết quả giải mã về chuỗi và trả về
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}
