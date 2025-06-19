package com.fiap.ponabri.security;

import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.repositories.UsuarioRepository;
import com.fiap.ponabri.security.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public OAuth2AuthenticationSuccessHandler(JwtUtils jwtUtils, UsuarioRepository usuarioRepository) {
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            // If email is not provided, handle error or fallback
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found from OAuth2 provider");
            return;
        }

        // Check if user exists, if not create new user with role USER
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();
        } else {
            usuario = new Usuario(
                (String) oAuth2User.getAttribute("name"),
                email,
                "USER"
            );
            usuarioRepository.save(usuario);
        }

        // Generate JWT token
        String token = jwtUtils.generateJwtToken(email);

        // Return token in response header or body (here in header)
        response.setHeader("Authorization", "Bearer " + token);

        // You can also redirect to frontend with token as parameter or handle as needed
        response.sendRedirect("/home?token=" + token);
    }
}
