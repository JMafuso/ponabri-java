package com.fiap.ponabri.services;

import org.springframework.stereotype.Service;

@Service
public class AbrigoAIService {

    public String sugerirCategoria(String descricao) {
        if (descricao == null) {
            return "Indefinida";
        }
        String descLower = descricao.toLowerCase();
        if (descLower.contains("crianças") || descLower.contains("familia")) {
            return "Familiar";
        } else if (descLower.contains("idosos") || descLower.contains("terceira idade")) {
            return "Idosos";
        } else if (descLower.contains("animais")) {
            return "Animais";
        } else {
            return "Geral";
        }
    }
}
