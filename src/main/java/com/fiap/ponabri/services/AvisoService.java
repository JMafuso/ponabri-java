package com.fiap.ponabri.services;

import com.fiap.ponabri.entities.Aviso;
import com.fiap.ponabri.repositories.AvisoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AvisoService {

    private final AvisoRepository avisoRepository;

    @Autowired
    public AvisoService(AvisoRepository avisoRepository) {
        this.avisoRepository = avisoRepository;
    }

    public Aviso save(Aviso aviso) {
        if (aviso.getDataCriacao() == null) {
            aviso.setDataCriacao(LocalDateTime.now());
        }

        // Placeholder for AI categorization logic
        aviso.setCategoriaSugerida("Categoria padrão");

        return avisoRepository.save(aviso);
    }
}
