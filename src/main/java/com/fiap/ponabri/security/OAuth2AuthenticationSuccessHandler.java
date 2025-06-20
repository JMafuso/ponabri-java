package com.fiap.ponabri.security;

import com.fiap.ponabri.entities.Usuario;
import com.fiap.ponabri.repositories.UsuarioRepository;
import com.fiap.ponabri.security.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationContext applicationContext;

    public OAuth2AuthenticationSuccessHandler(JwtUtils jwtUtils, UsuarioRepository usuarioRepository, ApplicationContext applicationContext) {
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.applicationContext = applicationContext;
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

        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);

        // Check if user exists, if not create new user with role USER and default password
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
            usuario.setSenha(passwordEncoder.encode("oauth2user"));
            usuarioRepository.save(usuario);
        }

        // Generate JWT token
        String token = jwtUtils.generateJwtToken(email);

        // Redirect to frontend to oauth2-login page with token as parameter
        response.sendRedirect("/oauth2-login?token=" + token);
    }
}
