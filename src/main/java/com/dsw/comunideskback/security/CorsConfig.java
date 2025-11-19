package com.dsw.comunideskback.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing)
 *
 * Esta abordagem usa um Bean de CorsConfigurationSource, que se integra
 * perfeitamente com o Spring Security (http.cors(withDefaults())).
 *
 * Isto substitui a abordagem de WebMvcConfigurer, que pode ter conflitos
 * com o bloqueio de requisições "preflight" (OPTIONS) do Spring Security.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // --- MUDANÇA CRÍTICA ---
        // Estamos a permitir a origem exata do nosso frontend Vue.js
        // que (graças ao vue.config.js) corre na porta 5173.
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // Métodos permitidos (incluindo OPTIONS para o preflight)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cabeçalhos permitidos (incluindo o 'Authorization' para o JWT)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        
        // Permite o envio de credenciais (como cookies ou tokens de autorização)
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a TODOS os endpoints /api/**
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}