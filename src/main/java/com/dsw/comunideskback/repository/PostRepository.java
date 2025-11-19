package com.dsw.comunideskback.repository;

import com.dsw.comunideskback.model.Post;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // O Spring Data JPA cria automaticamente os métodos:
    // save() -> Cria e Atualiza
    // findById() -> Busca por ID
    // findAll() -> Busca Todos
    // deleteById() -> Deleta por ID
    // ....
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.ativo = false WHERE p.ativo = true AND p.dataCriacao < :dataLimite")
    void desativarPostsAntigos(@Param("dataLimite") LocalDateTime dataLimite);
}