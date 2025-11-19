package com.dsw.comunideskback.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dsw.comunideskback.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Puxa o valor da nossa propriedade customizada no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("comunidesk-api") // Nome da aplicação
                    .withSubject(usuario.getLogin()) // O usuário que estamos logando
                    .withExpiresAt(genExpirationDate()) // Data de expiração
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Método para validar o token
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("comunidesk-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o login (subject) do usuário
        } catch (JWTVerificationException exception) {
            return ""; // Se o token for inválido, retorna vazio
        }
    }

    private Instant genExpirationDate() {
        // Token expira em 2 horas
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}