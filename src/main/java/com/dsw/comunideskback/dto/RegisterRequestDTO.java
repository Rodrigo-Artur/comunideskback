package com.dsw.comunideskback.dto;

import com.dsw.comunideskback.model.UsuarioRole;

public record RegisterRequestDTO(String login, String senha, UsuarioRole role) {
}