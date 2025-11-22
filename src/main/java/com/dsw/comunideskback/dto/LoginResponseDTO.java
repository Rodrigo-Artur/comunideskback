package com.dsw.comunideskback.dto;

import com.dsw.comunideskback.model.Usuario;

// Este DTO envia o token e o objeto usuário de volta para o frontend
public record LoginResponseDTO(String token, Usuario user) {
}