package com.dsw.comunideskback.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore; 
import org.springframework.security.core.userdetails.UserDetails;

// IMPORTANTE: Certifique-se que o arquivo PostType.java existe neste pacote!
import com.dsw.comunideskback.model.enums.PostType;

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
    @JsonIgnoreProperties({"senha", "role", "posts"}) 
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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    // --- MÉTODOS DE COMPATIBILIDADE PARA O CONTROLLER ---

    // O Controller chama getUser() em alguns lugares
    public Usuario getUser() { 
        return usuario; 
    }

    // O Controller chama setUser(Usuario)
    public void setUser(Usuario usuario) { 
        this.usuario = usuario; 
    }
    
    // O Controller tenta passar UserDetails diretamente.
    // Este método resolve o erro "incompatible types: UserDetails cannot be converted to Usuario"
    @JsonIgnore
    public void setUsuario(UserDetails userDetails) {
        if (userDetails instanceof Usuario) {
            this.usuario = (Usuario) userDetails;
        }
    }

    // Versão alternativa caso o Controller chame setUser(UserDetails)
    @JsonIgnore
    public void setUser(UserDetails userDetails) {
        if (userDetails instanceof Usuario) {
            this.usuario = (Usuario) userDetails;
        }
    }
}