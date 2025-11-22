package com.dsw.comunideskback.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore; 
import com.fasterxml.jackson.annotation.JsonProperty; // Importante
import org.springframework.security.core.userdetails.UserDetails;
import com.dsw.comunideskback.model.PostType;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    
    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private PostType category; 

    private Boolean ativo = true;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"senha", "role", "posts", "authorities", "password", "username", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"}) 
    private Usuario usuario;

    public Post() {}

    // --- GETTERS E SETTERS ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public PostType getCategory() { return category; }
    public void setCategory(PostType category) { this.category = category; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    // --- JSON PROPERTY: Força o nome "autor" no JSON ---
    
    @JsonProperty("autor") 
    public Usuario getUsuario() { 
        return usuario; 
    }

    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    // Métodos de compatibilidade e UserDetails
    
    @JsonIgnore
    public Usuario getUser() { return usuario; }
    
    @JsonIgnore
    public void setUser(Object principal) {
        if (principal instanceof Usuario) {
            this.usuario = (Usuario) principal;
        }
    }
    
    @JsonIgnore
    public void setUsuario(Object principal) {
        if (principal instanceof Usuario) {
            this.usuario = (Usuario) principal;
        }
    }
}