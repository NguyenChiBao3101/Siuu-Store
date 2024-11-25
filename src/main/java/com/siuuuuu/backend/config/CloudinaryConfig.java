package com.siuuuuu.backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ducww5vny",
                "api_key", "956421118896858",
                "api_secret", "t87wQN3mjmFJfSsQUuZEMlD_meE"));
    }
}