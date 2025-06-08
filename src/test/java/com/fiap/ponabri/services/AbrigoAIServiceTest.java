package com.fiap.ponabri.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbrigoAIServiceTest {

    private final AbrigoAIService abrigoAIService = new AbrigoAIService();

    @Test
    public void testSugerirCategoriaComDescricaoNula() {
        String categoria = abrigoAIService.sugerirCategoria(null);
        assertEquals("Categoria Indefinida", categoria);
    }

    @Test
    public void testSugerirCategoriaComDescricaoVazia() {
        String categoria = abrigoAIService.sugerirCategoria("");
        assertEquals("Categoria Indefinida", categoria);
    }

    @Test
    public void testSugerirCategoriaComDescricaoCrianca() {
        String categoria = abrigoAIService.sugerirCategoria("Abrigo para crianças e famílias");
        assertEquals("Abrigo Infantil", categoria);
    }

    @Test
    public void testSugerirCategoriaComDescricaoIdoso() {
        String categoria = abrigoAIService.sugerirCategoria("Abrigo para terceira idade");
        assertEquals("Abrigo para Idosos", categoria);
    }

    @Test
    public void testSugerirCategoriaComDescricaoAnimal() {
        String categoria = abrigoAIService.sugerirCategoria("Abrigo para animais abandonados");
        assertEquals("Abrigo Animal", categoria);
    }

    @Test
    public void testSugerirCategoriaComDescricaoGeral() {
        String categoria = abrigoAIService.sugerirCategoria("Abrigo comunitário geral");
        assertEquals("Abrigo Geral", categoria);
    }
}
