package com.fiap.ponabri.controllers;

import com.fiap.ponabri.dto.JwtResponseDto;
import com.fiap.ponabri.dto.UsuarioLoginDto;
import com.fiap.ponabri.dto.UsuarioRegisterDto;
import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.repositories.UsuarioRepository;
import com.fiap.ponabri.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioRegisterDto registerDto) {
        if (usuarioRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
        }
        if (!registerDto.getSenha().equals(registerDto.getConfirmarSenha())) {
            return ResponseEntity.badRequest().body("Erro: Senhas não conferem!");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(registerDto.getNome());
        usuario.setEmail(registerDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(registerDto.getSenha()));
        // Define a role padrão como USER para novos usuários
        usuario.setRole("USER");
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UsuarioLoginDto loginDto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginDto.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: Usuário não encontrado!");
        }
        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(loginDto.getSenha(), usuario.getSenha())) {
            return ResponseEntity.badRequest().body("Erro: Senha incorreta!");
        }
        String token = jwtUtils.generateJwtToken(usuario.getEmail());
        JwtResponseDto jwtResponse = new JwtResponseDto(token, usuario.getId(), usuario.getNome(), usuario.getEmail());
        return ResponseEntity.ok(jwtResponse);
    }
}
