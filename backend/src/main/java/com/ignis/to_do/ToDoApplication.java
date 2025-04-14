package com.ignis.to_do;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ToDoApplication {
	    
	static {
        // Carrega variáveis do arquivo .env

        Dotenv dotenv = Dotenv.configure()
        .directory(System.getProperty("user.dir")) // Define o diretório raiz
        .load();
        System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
        System.setProperty("spring.datasource.jwtSecretKey", dotenv.get("JWT_SECRET_KEY"));
        System.setProperty("spring.datasource.expirationTime", dotenv.get("EXPIRATION_TIME"));
    }

	public static void main(String[] args) {
		SpringApplication.run(ToDoApplication.class, args);
	}
}