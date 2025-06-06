package com.fiap.ponabri.dto;

import com.fiap.ponabri.enums.AbrigoStatus;
import lombok.Data;

@Data
public class AbrigoResponseDto {

    private Long id;
    private String nomeLocal;
    private String endereco;
    private String regiao;
    private Integer capacidadePessoas;
    private Integer vagasPessoasDisponiveis;
    private Integer capacidadeCarros;
    private Integer vagasCarrosDisponiveis;
    private String contatoResponsavel;
    private AbrigoStatus status;
    private String descricao;
    private String categoriaSugeridaAI;
}
