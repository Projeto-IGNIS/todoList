package com.ignis.to_do.validator;

import org.springframework.stereotype.Component;

import com.ignis.to_do.repository.UserRepository;

@Component
public class UserValidator {

    UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }   

    public boolean validateUser(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }  
    
}
