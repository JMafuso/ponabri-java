package com.fiap.ponabri.services;

import org.springframework.stereotype.Service;

@Service
public class AbrigoAIService {

    /**
     * Método que sugere uma categoria para o abrigo baseado na descrição.
     * Aqui pode ser implementada a integração com IA real.
     * Por enquanto, retorna uma categoria fixa para demonstração.
     */
    public String sugerirCategoria(String descricao) {
        if (descricao == null || descricao.isEmpty()) {
            return "Categoria Indefinida";
        }
        // Exemplo simples de sugestão baseada em palavras-chave
        String descLower = descricao.toLowerCase();
        if (descLower.contains("crianca") || descLower.contains("criança") || descLower.contains("infantil")) {
            return "Abrigo Infantil";
        } else if (descLower.contains("idoso") || descLower.contains("terceira idade")) {
            return "Abrigo para Idosos";
        } else if (descLower.contains("animal") || descLower.contains("animais")) {
            return "Abrigo Animal";
        } else {
            return "Abrigo Geral";
        }
    }
}
