package com.fiap.ponabri.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.ponabri.dto.UsuarioLoginDto;
import com.fiap.ponabri.dto.UsuarioRegisterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class FunctionalTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private Long usuarioId;

    @BeforeEach
    public void setup() throws Exception {
        // Register a user with unique email using timestamp
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        UsuarioRegisterDto registerDto = new UsuarioRegisterDto();
        registerDto.setNome("Test User");
        registerDto.setEmail(uniqueEmail);
        registerDto.setSenha("password123");
        registerDto.setConfirmarSenha("password123");

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Login to get JWT token
        UsuarioLoginDto loginDto = new UsuarioLoginDto();
        loginDto.setEmail(uniqueEmail);
        loginDto.setSenha("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponseBody = loginResult.getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(loginResponseBody).get("token").asText();
        assertThat(jwtToken).isNotEmpty();

        // Extract user ID from login response
        usuarioId = objectMapper.readTree(loginResponseBody).get("id").asLong();
    }

    @Test
    public void testAccessProtectedEndpointWithToken() throws Exception {
        mockMvc.perform(get("/api/abrigos")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/abrigos"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateAbrigo() throws Exception {
        String abrigoJson = """
            {
                "nomeLocal": "Abrigo Teste",
                "endereco": "Rua Teste, 123",
                "regiao": "Centro",
                "capacidadePessoas": 50,
                "capacidadeCarros": 10,
                "contatoResponsavel": "Contato Teste",
                "descricao": "Descrição do abrigo teste"
            }
            """;

        mockMvc.perform(post("/api/abrigos")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(abrigoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeLocal").value("Abrigo Teste"));

        // Register a user directly in the database for testing persistence
        String uniqueEmail = "persistencetest" + System.currentTimeMillis() + "@example.com";
        UsuarioRegisterDto registerDto = new UsuarioRegisterDto();
        registerDto.setNome("Persistence Test User");
        registerDto.setEmail(uniqueEmail);
        registerDto.setSenha("password123");
        registerDto.setConfirmarSenha("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateReserva() throws Exception {
        // First create an abrigo to get its ID
        String abrigoJson = """
            {
                "nomeLocal": "Abrigo Reserva",
                "endereco": "Rua Reserva, 456",
                "regiao": "Zona Sul",
                "capacidadePessoas": 30,
                "capacidadeCarros": 5,
                "contatoResponsavel": "Contato Reserva",
                "descricao": "Abrigo para reserva"
            }
            """;

        MvcResult abrigoResult = mockMvc.perform(post("/api/abrigos")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(abrigoJson))
                .andExpect(status().isOk())
                .andReturn();

        String abrigoResponse = abrigoResult.getResponse().getContentAsString();
        Long abrigoId = objectMapper.readTree(abrigoResponse).get("id").asLong();

        // Create reserva JSON
        String reservaJson = String.format("""
            {
                "usuarioId": %d,
                "abrigoId": %d,
                "dataReserva": "2025-12-31",
                "usouVagaCarro": false,
                "quantidadePessoas": 1
            }
            """, usuarioId, abrigoId);

        mockMvc.perform(post("/api/reservas")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abrigoId").value(abrigoId));
    }
}
