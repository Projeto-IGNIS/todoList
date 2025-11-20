package com.ignis.to_do.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ignis.to_do.dto.UserDTO;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    private Key getSigningKey() {
        if (jwtSecretKey == null || jwtSecretKey.length() < 32) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable is not set or too short");
        }
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public String generateToken(UserDTO userDTO) {

        String email = userDTO.getEmail();
        
        return Jwts.builder()
                .setSubject(email)
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
