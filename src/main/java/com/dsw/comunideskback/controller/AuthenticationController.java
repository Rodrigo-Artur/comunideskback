package com.dsw.comunideskback.controller;

import com.dsw.comunideskback.dto.LoginRequestDTO;
import com.dsw.comunideskback.dto.LoginResponseDTO;
import com.dsw.comunideskback.dto.RegisterRequestDTO;
import com.dsw.comunideskback.model.Usuario;
import com.dsw.comunideskback.repository.UsuarioRepository;
import com.dsw.comunideskback.service.TokenService;
import jakarta.validation.Valid; // Agora o Maven vai encontrar isso
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data){
        // Se o LoginRequestDTO usar 'password', chamamos .password()
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        // Agora o DTO aceita (String, Usuario), então isso vai compilar
        return ResponseEntity.ok(new LoginResponseDTO(token, (Usuario) auth.getPrincipal()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO data){
        if(this.repository.findByLogin(data.login()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: Login já está em uso!");
        }

        // Se o RegisterRequestDTO usar 'password', chamamos .password()
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        
        // Assume que o construtor do Usuario aceita (login, senha, role)
        Usuario newUser = new Usuario(data.login(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}