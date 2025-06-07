package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.AbrigoCreateDto;
import com.fiap.ponabri.dto.AbrigoResponseDto;
import com.fiap.ponabri.entities.Abrigo;
import com.fiap.ponabri.enums.AbrigoStatus;
import com.fiap.ponabri.repositories.AbrigoRepository;
import com.fiap.ponabri.repositories.ReservaRepository;
import com.fiap.ponabri.services.AbrigoAIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/abrigos")
public class AbrigosController {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private AbrigoAIService abrigoAIService;

    @Autowired
    private ReservaRepository reservaRepository;

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

    @GetMapping
    public ResponseEntity<List<AbrigoResponseDto>> listarAbrigos() {
        List<Abrigo> abrigos = abrigoRepository.findAll();
        List<AbrigoResponseDto> dtos = abrigos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbrigoResponseDto> obterAbrigo(@PathVariable Long id) {
        return abrigoRepository.findById(id)
                .map(abrigo -> ResponseEntity.ok(toResponseDto(abrigo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbrigoResponseDto> atualizarAbrigo(@PathVariable Long id, @Valid @RequestBody AbrigoCreateDto dto) {
        return abrigoRepository.findById(id)
                .map(abrigo -> {
                    abrigo.setNomeLocal(dto.getNomeLocal());
                    abrigo.setEndereco(dto.getEndereco());
                    abrigo.setRegiao(dto.getRegiao());
                    abrigo.setCapacidadePessoas(dto.getCapacidadePessoas());
                    abrigo.setVagasPessoasDisponiveis(abrigo.getCapacidadePessoas());
                    abrigo.setCapacidadeCarros(abrigo.getCapacidadeCarros());
                    abrigo.setVagasCarrosDisponiveis(abrigo.getCapacidadeCarros());
                    abrigo.setContatoResponsavel(dto.getContatoResponsavel());
                    abrigo.setDescricao(dto.getDescricao());
                    abrigo.setCategoriaSugeridaAI(abrigoAIService.sugerirCategoria(dto.getDescricao()));
                    Abrigo atualizado = abrigoRepository.save(abrigo);
                    return ResponseEntity.ok(toResponseDto(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAbrigo(@PathVariable Long id) {
        if (!abrigoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        boolean hasReservas = reservaRepository.findAll().stream()
                .anyMatch(reserva -> reserva.getAbrigo() != null && reserva.getAbrigo().getId().equals(id));
        if (hasReservas) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        abrigoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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
