package com.fiap.ponabri.dto;

import com.fiap.ponabri.enums.ReservaStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaResponseDto {

    private Long id;
    private String codigoReserva;
    private Long usuarioId;
    private Long abrigoId;
    private Integer quantidadePessoas;
    private Boolean usouVagaCarro;
    private LocalDateTime dataCriacao;
    private ReservaStatus status;
}
