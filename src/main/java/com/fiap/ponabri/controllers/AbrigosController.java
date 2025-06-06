package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.AbrigoCreateDto;
import com.fiap.ponabri.dto.AbrigoResponseDto;
import com.fiap.ponabri.entities.Abrigo;
import com.fiap.ponabri.enums.AbrigoStatus;
import com.fiap.ponabri.repositories.AbrigoRepository;
import com.fiap.ponabri.services.AbrigoAIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

// Removed org.springframework.stereotype.Controller and org.springframework.web.bind.annotation.GetMapping
// as @RestController already includes @Controller features and GetMapping is handled by @RequestMapping.

@RestController // Keep only @RestController for REST APIs
@RequestMapping("/api/abrigos") // This is the base path for all endpoints in this controller
public class AbrigosController {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private AbrigoAIService abrigoAIService;

    // Remove or comment out this method if you are building a pure REST API backend
    // and do not intend to serve Thymeleaf views from this controller.
    /*
    @GetMapping("/abrigos") // This would map to /api/abrigos/abrigos
    public String abrigosPage() {
        return "abrigos";
    }
    */

    @PostMapping
    public ResponseEntity<AbrigoResponseDto> criarAbrigo(@Valid @RequestBody AbrigoCreateDto dto) {
        Abrigo abrigo = Abrigo.builder()
                .nomeLocal(dto.getNomeLocal())
                .endereco(dto.getEndereco())
                .regiao(dto.getRegiao())
                .capacidadePessoas(dto.getCapacidadePessoas())
                .vagasPessoasDisponiveis(dto.getCapacidadePessoas())
                .capacidadeCarros(dto.getCapacidadeCarros())
                .vagasCarrosDisponiveis(dto.getCapacidadeCarros())
                .contatoResponsavel(dto.getContatoResponsavel())
                .descricao(dto.getDescricao())
                .status(AbrigoStatus.ATIVO)
                .categoriaSugeridaAI(abrigoAIService.sugerirCategoria(dto.getDescricao()))
                .build();

        Abrigo salvo = abrigoRepository.save(abrigo);

        return ResponseEntity.ok(toResponseDto(salvo));
    }

    @GetMapping // This method maps to GET /api/abrigos
    public ResponseEntity<List<AbrigoResponseDto>> listarAbrigos() {
        List<Abrigo> abrigos = abrigoRepository.findAll();
        List<AbrigoResponseDto> dtos = abrigos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}") // This method maps to GET /api/abrigos/{id}
    public ResponseEntity<AbrigoResponseDto> obterAbrigo(@PathVariable Long id) {
        return abrigoRepository.findById(id)
                .map(abrigo -> ResponseEntity.ok(toResponseDto(abrigo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // This method maps to PUT /api/abrigos/{id}
    public ResponseEntity<AbrigoResponseDto> atualizarAbrigo(@PathVariable Long id, @Valid @RequestBody AbrigoCreateDto dto) {
        return abrigoRepository.findById(id)
                .map(abrigo -> {
                    abrigo.setNomeLocal(dto.getNomeLocal());
                    abrigo.setEndereco(dto.getEndereco());
                    abrigo.setRegiao(dto.getRegiao());
                    abrigo.setCapacidadePessoas(dto.getCapacidadePessoas());
                    abrigo.setVagasPessoasDisponiveis(abrigo.getCapacidadePessoas()); // reset vagas
                    abrigo.setCapacidadeCarros(abrigo.getCapacidadeCarros());
                    abrigo.setVagasCarrosDisponiveis(abrigo.getCapacidadeCarros()); // reset vagas
                    abrigo.setContatoResponsavel(dto.getContatoResponsavel());
                    abrigo.setDescricao(dto.getDescricao());
                    abrigo.setCategoriaSugeridaAI(abrigoAIService.sugerirCategoria(dto.getDescricao()));
                    Abrigo atualizado = abrigoRepository.save(abrigo);
                    return ResponseEntity.ok(toResponseDto(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // This method maps to DELETE /api/abrigos/{id}
    public ResponseEntity<Void> deletarAbrigo(@PathVariable Long id) {
        if (abrigoRepository.existsById(id)) {
            abrigoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private AbrigoResponseDto toResponseDto(Abrigo abrigo) {
        AbrigoResponseDto dto = new AbrigoResponseDto();
        dto.setId(abrigo.getId());
        dto.setNomeLocal(abrigo.getNomeLocal());
        dto.setEndereco(abrigo.getEndereco());
        dto.setRegiao(abrigo.getRegiao());
        dto.setCapacidadePessoas(abrigo.getCapacidadePessoas());
        dto.setVagasPessoasDisponiveis(abrigo.getVagasPessoasDisponiveis());
        dto.setCapacidadeCarros(abrigo.getCapacidadeCarros());
        dto.setVagasCarrosDisponiveis(abrigo.getVagasCarrosDisponiveis());
        dto.setContatoResponsavel(abrigo.getContatoResponsavel());
        dto.setStatus(abrigo.getStatus());
        dto.setDescricao(abrigo.getDescricao());
        dto.setCategoriaSugeridaAI(abrigo.getCategoriaSugeridaAI());
        return dto;
    }
}