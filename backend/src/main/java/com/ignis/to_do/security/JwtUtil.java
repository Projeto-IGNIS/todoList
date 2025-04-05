package com.ignis.to_do.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.ignis.to_do.dto.UserDTO;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Dotenv dotenv;

    public JwtUtil(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(dotenv.get("JWT_SECRET_KEY").getBytes());
    }

    public String generateToken(UserDTO userDTO) {

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        
        return Jwts.builder()
                .setSubject(email)
                .setSubject(password)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(dotenv.get("EXPIRATION_TIME"))))
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
