package com.ignis.to_do.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ignis.to_do.dto.UserDTO;
import com.ignis.to_do.model.User;
import com.ignis.to_do.repository.UserRepository;
import com.ignis.to_do.validator.UserValidator;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vinicius Dias", "vinicius@email.com", "123456", null);
        userDTO = new UserDTO(1L, "Vinicius Dias", "vinicius@email.com", "123456");
    }

    @Test
    void testCreateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.getName(), createdUser.getName());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserDTOById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.getUserDTOById(1L);

        assertNotNull(foundUser);
        assertEquals(userDTO.getName(), foundUser.getName());
        assertEquals(userDTO.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsers() {

        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOs = (List<UserDTO>) userService.getAllUsers();

        assertNotNull(userDTOs);
        assertEquals(1, userDTOs.size());
        assertEquals(userDTO.getName(), userDTOs.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).updateUser(
            1L, "Novo Nome", "novo@email.com", "654321");

        UserDTO updatedUser = userService.updateUserById(new UserDTO(1L, "Novo Nome", "novo@email.com", "654321"));

        assertNotNull(updatedUser);
        assertEquals("Novo Nome", updatedUser.getName());
        assertEquals("novo@email.com", updatedUser.getEmail());
        verify(userRepository, times(1)).updateUser(1L, "Novo Nome", "novo@email.com", "654321");
    }

    @Test
    void testDeleteUserById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testValidateUser() {

        when(userRepository.existsByEmailAndPassword("testing@gmail", "1234")).thenReturn(true);

        boolean isValid = userValidator.validateUser("testing@gmail", "1234");

        assertTrue(isValid);
        verify(userRepository, times(1)).existsByEmailAndPassword("testing@gmail", "1234");
    }

    @Test
    void testGetUser() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    
        User foundUser = userService.getUser(1L);
    
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        verify(userRepository, times(2)).findById(1L);
    }
}
