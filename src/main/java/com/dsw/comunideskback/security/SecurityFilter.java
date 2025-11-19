package com.dsw.comunideskback.security;

import com.dsw.comunideskback.repository.UsuarioRepository;
import com.dsw.comunideskback.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Esta classe será um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        if (token != null) {
            // Se houver token, valida
            var login = tokenService.validateToken(token);
            UserDetails user = usuarioRepository.findByLogin(login);

            if (user != null) {
                // Se o usuário for válido, o autentica no contexto do Spring
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // Continua para o próximo filtro (ou para o controller)
        filterChain.doFilter(request, response);
    }

    // Método para extrair o token do Header "Authorization"
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        // O token vem como "Bearer <token>", então removemos o "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}