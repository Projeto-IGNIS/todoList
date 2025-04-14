package com.ignis.to_do.controller;

import com.ignis.to_do.dto.UserDTO;
import com.ignis.to_do.service.UserService;
import com.ignis.to_do.validator.UserValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "João", "joao@email.com", "senha123");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users/createUser")
                        .contentType("application/json")
                        .content("{\"name\":\"João\",\"email\":\"joao@email.com\",\"password\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.password").value("senha123"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void testLogin() throws Exception {

        when(userValidator.validateUser("joao@email.com", "senha123")).thenReturn(true);

        mockMvc.perform(post("/users/login")
                            .contentType("application/json")
                            .content("{\"name\":\"João\",\"email\":\"joao@email.com\",\"password\":\"senha123\"}"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logado com sucesso"));

        verify(userValidator, times(1)).validateUser("joao@email.com", "senha123");
    }
}
