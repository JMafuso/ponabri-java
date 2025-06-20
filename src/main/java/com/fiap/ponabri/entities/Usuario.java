package com.fiap.ponabri.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String senha;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "usuario")
    private Set<Reserva> reservas;

    public Usuario(String nome, String email, String role) {
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
