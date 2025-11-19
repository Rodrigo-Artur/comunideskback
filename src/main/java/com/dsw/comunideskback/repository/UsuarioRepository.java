package com.dsw.comunideskback.repository;

import com.dsw.comunideskback.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Este método é o mais importante para a autenticação.
     * O Spring Security (através do nosso AuthorizationService) vai usá-lo
     * para buscar um usuário pelo seu campo de 'login' (que implementa 'getUsername').
     */
    UserDetails findByLogin(String login);
}