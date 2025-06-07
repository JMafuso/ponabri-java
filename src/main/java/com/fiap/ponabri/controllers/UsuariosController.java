package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.*;
import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuariosController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/register")
    public String register(org.springframework.ui.Model model) {
        model.addAttribute("usuarioRegisterDto", new com.fiap.ponabri.dto.UsuarioRegisterDto());
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/usuarios")
    @ResponseBody
    public java.util.List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/registrar")
    @org.springframework.transaction.annotation.Transactional
    public String registrarUsuario(@Valid @ModelAttribute UsuarioRegisterDto usuarioDto, org.springframework.validation.BindingResult bindingResult) {
        System.out.println("Tentativa de registro para email: " + usuarioDto.getEmail());
        if (bindingResult.hasErrors()) {
            System.out.println("Erro de validação no registro.");
            return "register";
        }
        if (usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent()) {
            System.out.println("Email já cadastrado: " + usuarioDto.getEmail());
            return "redirect:/register?error=email";
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.getNome())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha())
                .role("USER")
                .build();

        usuarioRepository.save(usuario);
        System.out.println("Registro bem-sucedido para email: " + usuarioDto.getEmail());

        return "redirect:/login?success";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UsuarioLoginDto loginDto) {
        System.out.println("Tentativa de login para email: " + loginDto.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(loginDto.getEmail()).orElse(null);
        if (usuario != null) {
            System.out.println("Usuário encontrado: " + usuario.getEmail());
            if (usuario.getSenha().equals(loginDto.getSenha())) {
                System.out.println("Senha correta. Login bem-sucedido.");
                return "redirect:/home";
            } else {
                System.out.println("Senha incorreta.");
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
        return "redirect:/login?error";
    }
}
