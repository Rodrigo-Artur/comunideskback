package com.dsw.comunideskback.repository;

import com.dsw.comunideskback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
// MUDANÇA: Assumindo que o ID do Usuario também é String (UUID).
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    // MUDANÇA CRÍTICA: O PostController usa este método para buscar o autor do post.
    Optional<Usuario> findByLogin(String login);
}