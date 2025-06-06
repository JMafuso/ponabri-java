package com.fiap.ponabri.entities;

import com.fiap.ponabri.enums.AbrigoStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "abrigos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abrigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeLocal;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String regiao;

    @Column(nullable = false)
    private Integer capacidadePessoas;

    @Column(nullable = false)
    private Integer vagasPessoasDisponiveis;

    @Column(nullable = false)
    private Integer capacidadeCarros;

    @Column(nullable = false)
    private Integer vagasCarrosDisponiveis;

    @Column(nullable = false)
    private String contatoResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbrigoStatus status;

    @Column(length = 1000)
    private String descricao;

    private String categoriaSugeridaAI;

    @OneToMany(mappedBy = "abrigo")
    private Set<Reserva> reservas;
}
