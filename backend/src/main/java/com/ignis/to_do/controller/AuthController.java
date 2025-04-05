package com.ignis.to_do.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignis.to_do.dto.UserDTO;
import com.ignis.to_do.security.JwtUtil;
import com.ignis.to_do.service.UserService;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {

        if (userService.verifyIfUserExists(userDTO)){
            String token = jwtUtil.generateToken(userDTO);
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Usuário não encontrado");   

    }

    @GetMapping("/ownerId/{email}")
    public ResponseEntity<String> getOwnerId(@PathVariable String email) {

        return ResponseEntity.ok(userService.getOwnerId(email));
    }
}
