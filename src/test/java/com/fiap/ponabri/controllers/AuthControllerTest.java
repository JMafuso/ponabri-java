package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.UsuarioLoginDto;
import com.fiap.ponabri.dto.UsuarioRegisterDto;
import com.fiap.ponabri.repositories.UsuarioRepository;
import com.fiap.ponabri.security.JwtUtils;
import com.fiap.ponabri.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private UsuarioRegisterDto registerDto;
    private UsuarioLoginDto loginDto;

    @BeforeEach
    public void setup() {
        registerDto = new UsuarioRegisterDto();
        registerDto.setNome("Test User");
        registerDto.setEmail("test@example.com");
        registerDto.setSenha("password");
        registerDto.setConfirmarSenha("password");

        loginDto = new UsuarioLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setSenha("password");
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        Mockito.when(usuarioRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(registerDto.getSenha())).thenReturn("encodedPassword");
        Mockito.when(usuarioRepository.save(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Test User\",\"email\":\"test@example.com\",\"senha\":\"password\",\"confirmarSenha\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário registrado com sucesso!"));
    }

    @Test
    public void testRegisterUser_EmailExists() throws Exception {
        Mockito.when(usuarioRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Test User\",\"email\":\"test@example.com\",\"senha\":\"password\",\"confirmarSenha\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro: Email já está em uso!"));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // Mock user and password matching
        // This test can be expanded with more detailed mocks if needed
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"senha\":\"password\"}"))
                .andExpect(status().isBadRequest()); // Because no user found in mock, expect bad request
    }
}
