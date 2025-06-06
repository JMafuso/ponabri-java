package com.fiap.ponabri.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRegisterDto {

    @NotBlank(message = "{usuario.nome.notblank}")
    private String nome;

    @Email(message = "{usuario.email.valid}")
    @NotBlank(message = "{usuario.email.notblank}")
    private String email;

    @NotBlank(message = "{usuario.senha.notblank}")
    @Size(min = 6, message = "{usuario.senha.size}")
    private String senha;
}
