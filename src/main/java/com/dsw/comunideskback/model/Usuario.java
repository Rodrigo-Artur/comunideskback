package com.dsw.comunideskback.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
// APLICA O FILTRO AUTOMÁTICO PARA TODAS AS CONSULTAS NESTA ENTIDADE
@SQLRestriction("ativo = true") 
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // O login deve ser único
    private String login; 

    @Column(nullable = false)
    private String senha; // Esta será a senha criptografada

    @Enumerated(EnumType.STRING) // Salva "ADMIN" ou "USER" no banco
    @Column(nullable = false)
    private UsuarioRole role;

    private boolean ativo = true; // Por padrão, todo novo usuário é ativo
    

    // Construtores
    public Usuario() {}

    public Usuario(String login, String senha, UsuarioRole role) {
        this.login = login;
        this.senha = senha;
        this.role = role;
        this.ativo = true; // Garantir que o construtor também defina como true
    }

    // --- MÉTODOS DO USERDETAILS ---
    // O Spring Security usa estes métodos internamente

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define as "permissões" do usuário
        // Se for ADMIN, ele tem a permissão "ROLE_ADMIN"
        // Se for USER, ele tem a permissão "ROLE_USER"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha
    }

    @Override
    public String getUsername() {
        return this.login; // Retorna o campo de login (username)
    }

    // Métodos que controlam o status da conta
    // Para um sistema simples, podemos deixar todos como 'true'
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }


    // --- Getters e Setters Padrão ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioRole getRole() {
        return role;
    }

    public void setRole(UsuarioRole role) {
        this.role = role;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
