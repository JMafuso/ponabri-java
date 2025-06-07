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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.fiap.ponabri.dto.AbrigoInfoDto;
import com.fiap.ponabri.dto.UsuarioInfoDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
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

    private static final String FILA_RESERVAS = "simpleQueue";

    @GetMapping("/reservas")
    public String reservasPage() {
        return "reservas";
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDto>> listarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaResponseDto> dtos = reservas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> criarReserva(@Valid @RequestBody ReservaCreateDto dto) {
        System.out.println("Received ReservaCreateDto: " + dto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
        Optional<Abrigo> abrigoOpt = abrigoRepository.findById(dto.getAbrigoId());
        if (abrigoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Abrigo não encontrado.");
        }

        Abrigo abrigo = abrigoOpt.get();

        if (dto.getQuantidadePessoas() > abrigo.getVagasPessoasDisponiveis()) {
            return ResponseEntity.badRequest().body("Não há vagas suficientes para pessoas.");
        } else if (dto.getUsouVagaCarro() && abrigo.getVagasCarrosDisponiveis() < 1) {
            return ResponseEntity.badRequest().body("Não há vagas de carro disponíveis.");
        }

        abrigo.setVagasPessoasDisponiveis(abrigo.getVagasPessoasDisponiveis() - dto.getQuantidadePessoas());
        if (dto.getUsouVagaCarro()) {
            abrigo.setVagasCarrosDisponiveis(abrigo.getVagasCarrosDisponiveis() - 1);
        }
        abrigoRepository.save(abrigo);

        String codigoReserva = "PONABRI-" + generateRandomAlphanumeric(8);

        Reserva reserva = Reserva.builder()
                .codigoReserva(codigoReserva)
                .usuario(usuario)
                .abrigo(abrigo)
                .quantidadePessoas(dto.getQuantidadePessoas())
                .usouVagaCarro(dto.getUsouVagaCarro())
                .dataCriacao(LocalDateTime.now())
                .status(ReservaStatus.PENDENTE)
                .build();

        Reserva salva = reservaRepository.save(reserva);

        rabbitTemplate.convertAndSend(FILA_RESERVAS, "Mensagem de teste para RabbitMQ");

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

        UsuarioInfoDto usuarioInfo = new UsuarioInfoDto();
        usuarioInfo.setId(reserva.getUsuario().getId());
        usuarioInfo.setNome(reserva.getUsuario().getNome());
        dto.setUsuarioInfo(usuarioInfo);

        dto.setAbrigoId(reserva.getAbrigo().getId());

        AbrigoInfoDto abrigoInfo = new AbrigoInfoDto();
        abrigoInfo.setId(reserva.getAbrigo().getId());
        abrigoInfo.setNomeLocal(reserva.getAbrigo().getNomeLocal());
        dto.setAbrigoInfo(abrigoInfo);

        dto.setQuantidadePessoas(reserva.getQuantidadePessoas());
        dto.setUsouVagaCarro(reserva.getUsouVagaCarro());
        dto.setDataCriacao(reserva.getDataCriacao());
        dto.setStatus(reserva.getStatus());
        return dto;
    }

    private String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reserva reserva = reservaOpt.get();

        Abrigo abrigo = reserva.getAbrigo();
        abrigo.setVagasPessoasDisponiveis(abrigo.getVagasPessoasDisponiveis() + reserva.getQuantidadePessoas());
        if (reserva.getUsouVagaCarro()) {
            abrigo.setVagasCarrosDisponiveis(abrigo.getVagasCarrosDisponiveis() + 1);
        }
        abrigoRepository.save(abrigo);

        reservaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
