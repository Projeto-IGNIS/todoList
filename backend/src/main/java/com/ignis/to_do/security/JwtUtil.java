package com.ignis.to_do.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.ignis.to_do.dto.UserDTO;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private Key getSigningKey() {
        String secret = System.getenv("JWT_SECRET_KEY");
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable is not set or too short");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDTO userDTO) {

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        
        String expirationTimeStr = System.getenv("EXPIRATION_TIME");
        if (expirationTimeStr == null) {
            throw new IllegalStateException("EXPIRATION_TIME environment variable is not set");
        }
        long expirationTime = Long.parseLong(expirationTimeStr);
        
        return Jwts.builder()
                .setSubject(email)
                .setSubject(password)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
