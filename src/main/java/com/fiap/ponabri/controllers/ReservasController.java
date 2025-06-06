package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.ReservaCreateDto;
import com.fiap.ponabri.dto.ReservaResponseDto;
import com.fiap.ponabri.entities.Abrigo;
import com.fiap.ponabri.entities.Reserva;
import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.enums.ReservaStatus;
import com.fiap.ponabri.repositories.AbrigoRepository;
import com.fiap.ponabri.repositories.ReservaRepository;
import com.fiap.ponabri.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Use apenas @RestController

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController // Mantenha apenas @RestController para controladores REST
@RequestMapping("/api/reservas")
public class ReservasController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String FILA_RESERVAS = "filaReservas";

    // Deixe apenas um método para /reservas GET, se ele realmente for para uma view Thymeleaf.
    // Se for uma API REST, este método geralmente não seria um `String`.
    @GetMapping("/reservas")
    public String reservasPage() {
        return "reservas"; // Assume que "reservas" é o nome de um template Thymeleaf
    }

    @PostMapping
    public ResponseEntity<?> criarReserva(@Valid @RequestBody ReservaCreateDto dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getUsuarioId());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
        Optional<Abrigo> abrigoOpt = abrigoRepository.findById(dto.getAbrigoId());
        if (abrigoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Abrigo não encontrado.");
        }

        Abrigo abrigo = abrigoOpt.get();

        // Verificar vagas disponíveis
        if (dto.getQuantidadePessoas() > abrigo.getVagasPessoasDisponiveis()) {
            return ResponseEntity.badRequest().body("Não há vagas suficientes para pessoas.");
        } else if (dto.getUsouVagaCarro() && abrigo.getVagasCarrosDisponiveis() < 1) {
            return ResponseEntity.badRequest().body("Não há vagas de carro disponíveis.");
        }

        // Decrementar vagas
        abrigo.setVagasPessoasDisponiveis(abrigo.getVagasPessoasDisponiveis() - dto.getQuantidadePessoas());
        if (dto.getUsouVagaCarro()) {
            abrigo.setVagasCarrosDisponiveis(abrigo.getVagasCarrosDisponiveis() - 1);
        }
        abrigoRepository.save(abrigo);

        // Gerar código reserva único
        String codigoReserva = UUID.randomUUID().toString();

        Reserva reserva = Reserva.builder()
                .codigoReserva(codigoReserva)
                .usuario(usuarioOpt.get())
                .abrigo(abrigo)
                .quantidadePessoas(dto.getQuantidadePessoas())
                .usouVagaCarro(dto.getUsouVagaCarro())
                .dataCriacao(LocalDateTime.now())
                .status(ReservaStatus.PENDENTE)
                .build();

        Reserva salva = reservaRepository.save(reserva);

        // Publicar mensagem na fila RabbitMQ
        rabbitTemplate.convertAndSend(FILA_RESERVAS, salva.getId().toString());

        return ResponseEntity.ok(toResponseDto(salva));
    }

    @GetMapping("/VALIDACAO/{codigoReserva}")
    public ResponseEntity<?> validarReserva(@PathVariable String codigoReserva) {
        Optional<Reserva> reservaOpt = reservaRepository.findByCodigoReserva(codigoReserva);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reserva reserva = reservaOpt.get();
        return ResponseEntity.ok(toResponseDto(reserva));
    }

    private ReservaResponseDto toResponseDto(Reserva reserva) {
        ReservaResponseDto dto = new ReservaResponseDto();
        dto.setId(reserva.getId());
        dto.setCodigoReserva(reserva.getCodigoReserva());
        dto.setUsuarioId(reserva.getUsuario().getId());
        dto.setAbrigoId(reserva.getAbrigo().getId());
        dto.setQuantidadePessoas(reserva.getQuantidadePessoas());
        dto.setUsouVagaCarro(reserva.getUsouVagaCarro());
        dto.setDataCriacao(reserva.getDataCriacao());
        dto.setStatus(reserva.getStatus());
        return dto;
    }
}