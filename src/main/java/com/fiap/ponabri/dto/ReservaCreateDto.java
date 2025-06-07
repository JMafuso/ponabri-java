package com.fiap.ponabri.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaCreateDto {

    @NotNull(message = "{reserva.abrigoId.notnull}")
    private Long abrigoId;

    @NotNull(message = "{reserva.quantidadePessoas.notnull}")
    @Min(value = 1, message = "{reserva.quantidadePessoas.min}")
    private Integer quantidadePessoas;

    @NotNull(message = "{reserva.usouVagaCarro.notnull}")
    private Boolean usouVagaCarro;
}
