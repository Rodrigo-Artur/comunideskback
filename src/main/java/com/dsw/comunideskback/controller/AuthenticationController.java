package com.dsw.comunideskback.controller;

import com.dsw.comunideskback.dto.LoginRequestDTO;
import com.dsw.comunideskback.dto.LoginResponseDTO;
import com.dsw.comunideskback.dto.RegisterRequestDTO;
import com.dsw.comunideskback.model.Usuario;
import com.dsw.comunideskback.repository.UsuarioRepository;
import com.dsw.comunideskback.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") // URL base para autenticação
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    /**
     * Endpoint para LOGIN de usuário.
     * URL: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
        // Cria o "pacote" de autenticação com login e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        
        // O Spring Security tenta autenticar
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Se der certo, gera o token
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        // Retorna o DTO de resposta com o token
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    /**
     * Endpoint para REGISTRO de novo usuário.
     * URL: POST /api/auth/register
     */
    @PostMapping("/register")
    // uma String (no erro) ou Vazio (no sucesso).
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO data) {
        // Verifica se o login já existe
        if (this.usuarioRepository.findByLogin(data.login()) != null) {
            // O corpo aqui é uma String
            return ResponseEntity.badRequest().body("Erro: Login já está em uso!");
        }

        // Criptografa a senha antes de salvar
        String encryptedPassword = passwordEncoder.encode(data.senha());
        
        // Cria o novo objeto Usuário
        Usuario novoUsuario = new Usuario(data.login(), encryptedPassword, data.role());

        // Salva o novo usuário no banco
        this.usuarioRepository.save(novoUsuario);

        // O corpo aqui é vazio (build())
        return ResponseEntity.ok().build(); // Retorna 200 OK (sem corpo)
    }
}