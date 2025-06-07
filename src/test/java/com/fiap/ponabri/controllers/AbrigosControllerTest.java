package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.AbrigoCreateDto;
import com.fiap.ponabri.dto.UsuarioLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AbrigosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    public void loginAsAdmin() throws Exception {
        UsuarioLoginDto loginDto = new UsuarioLoginDto();
        loginDto.setEmail("admin@admin.com");
        loginDto.setSenha("Admin!123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        // Extract token from response JSON
        jwtToken = objectMapper.readTree(response).get("token").asText();
        assertThat(jwtToken).isNotEmpty();
    }

    @Test
    public void testCreateAndDeleteAbrigo() throws Exception {
        AbrigoCreateDto abrigoCreateDto = new AbrigoCreateDto();
        abrigoCreateDto.setNomeLocal("Abrigo Teste");
        abrigoCreateDto.setEndereco("Rua Teste, 123");
        abrigoCreateDto.setRegiao("Centro");
        abrigoCreateDto.setCapacidadePessoas(10);
        abrigoCreateDto.setCapacidadeCarros(5);
        abrigoCreateDto.setContatoResponsavel("Contato Teste");
        abrigoCreateDto.setDescricao("Descrição do abrigo teste");

        // Create Abrigo
        MvcResult createResult = mockMvc.perform(post("/api/abrigos")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(abrigoCreateDto)))
                .andExpect(status().isOk())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        Long abrigoId = objectMapper.readTree(createResponse).get("id").asLong();
        assertThat(abrigoId).isNotNull();

        // Delete Abrigo
        mockMvc.perform(delete("/api/abrigos/" + abrigoId)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }
}
