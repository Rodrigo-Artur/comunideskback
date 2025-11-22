package com.dsw.comunideskback.repository;

import com.dsw.comunideskback.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Necessário para @Modifying
import org.springframework.data.jpa.repository.Query;     // Necessário para @Query
import org.springframework.transaction.annotation.Transactional; // Necessário para transações
import java.util.List;
import java.time.LocalDateTime; // Import para LocalDateTime

public interface PostRepository extends JpaRepository<Post, String> {
    
    List<Post> findByAtivoTrue();

    // MUDANÇA: Adicionando o método customizado que o PostExpirationService precisa
    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.ativo = false WHERE p.expirationDate < :dataLimite AND p.ativo = true")
    void desativarPostsAntigos(LocalDateTime dataLimite);
}