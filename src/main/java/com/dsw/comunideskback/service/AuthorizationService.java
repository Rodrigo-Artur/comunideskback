package com.dsw.comunideskback.service;

import com.dsw.comunideskback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O Spring vai chamar este método quando o usuário tentar logar
        // Ele usa o método findByLogin que criamos no UsuarioRepository
        return usuarioRepository.findByLogin(username);
    }
}