package com.example.common.configure;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloundDinaryConfig {

    @Value("${cloudinary.cloud_name:default_cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key:default_api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret:default_api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
            Map<String,String> config = new HashMap<>();
            config.put("cloud_name",cloudName );
            config.put("api_key", apiKey);
            config.put("api_secret",apiSecret);
            return new Cloudinary(config);
        }
}
