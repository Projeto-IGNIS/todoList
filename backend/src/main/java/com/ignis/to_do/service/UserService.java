package com.ignis.to_do.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ignis.to_do.dto.UserDTO;
import com.ignis.to_do.exception.user_exception.UserAlreadyExistsException;
import com.ignis.to_do.exception.user_exception.UserNotFoundException;
import com.ignis.to_do.model.User;
import com.ignis.to_do.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "Usuário com ID %s nao encontrado";
    private static final String USER_ALREADY_EXISTS = "Usuário com email %s já existe";


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }


    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getId() != null) {
<<<<<<< HEAD

            verifyIfUserExists(userDTO.getId());
=======
            verifyIfUserExists(userDTO);
>>>>>>> f792b8ef9935a18b7cf3675e3666045444f1856b
        } else if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS.formatted(userDTO.getEmail()));
        }

        User user = userRepository.save(new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword()));
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public UserDTO getUserDTOById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword()))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.formatted(userId)));
    }

    public String getOwnerId(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.formatted(email)));
        return String.valueOf(user.getId());
        
    }

    public boolean verifyIfUserExists(UserDTO userDTO) {

        if (userDTO.getId() != null) {
            userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND.formatted(userDTO.getId())));
            return true;
        }
        return userRepository.existsByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
    }

    public Iterable<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(user -> new UserDTO(user.getId(),
            user.getName(), user.getEmail(), user.getPassword())).toList();
    }

    
    @Transactional
    public UserDTO updateUserById(UserDTO userDTO) {

        verifyIfUserExists(userDTO);
        userRepository.updateUser(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
        return new UserDTO(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
    }



    public void deleteUserById(Long userId){ 

        User user = userRepository.findById(userId).orElseThrow(()
         -> new UserNotFoundException(USER_NOT_FOUND.formatted(userId)));
        userRepository.delete(user);
    }

    @Transactional
    public void updatePasswordById(UserDTO userDTO) {

        verifyIfUserExists(userDTO);
        userRepository.updatePasswordById(userDTO.getId(), userDTO.getPassword());
    }
    
    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()
         -> new UserNotFoundException(USER_NOT_FOUND.formatted(userId)));
    }

    public boolean loginVerify(UserDTO userDTO) {
        return userRepository.existsByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
    }
}
