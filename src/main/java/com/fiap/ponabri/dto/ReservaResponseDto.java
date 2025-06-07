package com.fiap.ponabri.dto;

import com.fiap.ponabri.enums.ReservaStatus;
import lombok.Data;
import java.time.LocalDateTime;
import com.fiap.ponabri.dto.AbrigoInfoDto;
import com.fiap.ponabri.dto.UsuarioInfoDto;

@Data
public class ReservaResponseDto {

    private Long id;
    private String codigoReserva;
    private Long usuarioId;
    private UsuarioInfoDto usuarioInfo;
    private Long abrigoId;
    private AbrigoInfoDto abrigoInfo;
    private Integer quantidadePessoas;
    private Boolean usouVagaCarro;
    private LocalDateTime dataCriacao;
    private ReservaStatus status;
}
