package com.siuuuuu.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecaptchaService {

    @Value("${google.recaptcha.secret-key}")
    private String secretKey;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    // Sử dụng Jackson ObjectMapper để phân tích JSON
    private final ObjectMapper objectMapper;

    public RecaptchaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean validateCaptcha(String responseToken) {
        if (responseToken == null || responseToken.isEmpty()) {
            return false;
        }

        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl(VERIFY_URL)
                    .queryParam("secret", secretKey)
                    .queryParam("response", responseToken)
                    .toUriString();

            // Tạo kết nối HTTP
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Gửi yêu cầu
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = "".getBytes();  // No body needed for this request
                os.write(input, 0, input.length);
            }

            // Đọc phản hồi từ Google Recaptcha
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Chuyển đổi phản hồi JSON thành Map
                Map<String, Object> response = objectMapper.readValue(content.toString(), Map.class);

                // Kiểm tra phản hồi
                return response != null && Boolean.TRUE.equals(response.get("success"));
            }
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi gọi API, log lỗi và trả về false
            e.printStackTrace();
            return false;
        }
    }
}
