package com.fiap.ponabri.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioLoginDto {

    @Email(message = "{usuario.email.valid}")
    @NotBlank(message = "{usuario.email.notblank}")
    private String email;

    @NotBlank(message = "{usuario.senha.notblank}")
    private String senha;
}
