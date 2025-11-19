package com.dsw.comunideskback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// <-- MUDANÇA 1: Importar o CorsConfigurationSource
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    // <-- MUDANÇA 2: Injetar o Bean do CorsConfig.java
    private final CorsConfigurationSource corsConfigurationSource;

    @Autowired
    public SecurityConfig(SecurityFilter securityFilter, CorsConfigurationSource corsConfigurationSource) {
        this.securityFilter = securityFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }
    // --- Fim da Mudança 2 ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // <-- MUDANÇA 3: Ligar a configuração de CORS
                // Isto diz ao Spring Security para usar as regras do CorsConfig.java
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Desabilita CSRF (Isto já estava correto)
                .csrf(csrf -> csrf.disable())
                
                // Define a política de sessão como STATELESS (Isto já estava correto)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Configura as regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        
                        // <-- MUDANÇA 4: Permitir requisições OPTIONS (preflight)
                        // Esta é a correção para o 403 (Forbidden)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // Permite acesso PÚBLICO aos endpoints de login e registro (Isto já estava correto)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        
                        // Todos os outros endpoints exigem autenticação (Isto já estava correto)
                        .anyRequest().authenticated()
                )
                
                // Adiciona nosso filtro (SecurityFilter) antes do filtro padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Expõe o AuthenticationManager para ser usado no Controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define o algoritmo de criptografia de senhas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}