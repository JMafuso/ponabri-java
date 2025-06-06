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

    // Removidos os métodos POST /registrar e /login para evitar conflito com AuthController REST
}
