package com.fiap.ponabri.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbrigoCreateDto {

    @NotBlank(message = "{abrigo.nomeLocal.notblank}")
    private String nomeLocal;

    @NotBlank(message = "{abrigo.endereco.notblank}")
    private String endereco;

    @NotBlank(message = "{abrigo.regiao.notblank}")
    private String regiao;

    @NotNull(message = "{abrigo.capacidadePessoas.notnull}")
    @Min(value = 1, message = "{abrigo.capacidadePessoas.min}")
    private Integer capacidadePessoas;

    @NotNull(message = "{abrigo.capacidadeCarros.notnull}")
    @Min(value = 0, message = "{abrigo.capacidadeCarros.min}")
    private Integer capacidadeCarros;

    @NotBlank(message = "{abrigo.contatoResponsavel.notblank}")
    private String contatoResponsavel;

    @NotBlank(message = "{abrigo.descricao.notblank}")
    private String descricao;
}
