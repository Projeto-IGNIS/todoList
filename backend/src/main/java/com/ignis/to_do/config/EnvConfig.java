package com.ignis.to_do.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    
    @Bean
    Dotenv dotenv() {
        return Dotenv.configure()
                .ignoreIfMissing().directory(System.getProperty("user.dir")+"/backend")
                .load();
    }
}