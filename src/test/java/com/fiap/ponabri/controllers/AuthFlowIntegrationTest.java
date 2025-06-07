package com.fiap.ponabri.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.ponabri.dto.UsuarioLoginDto;
import com.fiap.ponabri.dto.UsuarioRegisterDto;
import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private com.fiap.ponabri.repositories.ReservaRepository reservaRepository;

    @BeforeEach
    public void setup() {
        // Instead of deleting all users, only delete test data created by tests
        // For example, delete reservas and users with specific test email pattern
        reservaRepository.deleteAll();

        // Delete only users created for tests (e.g., emails containing "teste@fiap.com" or "testuser")
        usuarioRepository.findAll().stream()
            .filter(u -> u.getEmail().contains("teste@fiap.com") || u.getEmail().contains("testuser"))
            .forEach(u -> usuarioRepository.delete(u));
    }

    @Test
    public void testRegisterLoginAndAccessProtectedEndpoint() throws Exception {
        // Registro
        UsuarioRegisterDto registerDto = new UsuarioRegisterDto();
        registerDto.setNome("Teste Usuario");
        registerDto.setEmail("teste@fiap.com");
        registerDto.setSenha("123456");
        registerDto.setConfirmarSenha("123456");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Verifica usuário salvo com senha criptografada
        Usuario usuario = usuarioRepository.findByEmail("teste@fiap.com").orElse(null);
        assertThat(usuario).isNotNull();
        assertThat(passwordEncoder.matches("123456", usuario.getSenha())).isTrue();

        // Login
        UsuarioLoginDto loginDto = new UsuarioLoginDto();
        loginDto.setEmail("teste@fiap.com");
        loginDto.setSenha("123456");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        assertThat(responseBody).contains("token");

        // Extrai token JWT do response
        String token = objectMapper.readTree(responseBody).get("token").asText();
        assertThat(token).isNotEmpty();

        // Acesso a endpoint protegido (exemplo: /api/abrigos)
        mockMvc.perform(get("/api/abrigos")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
