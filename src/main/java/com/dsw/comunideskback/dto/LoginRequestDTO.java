package com.dsw.comunideskback.dto;

// Este DTO recebe o login e a senha do frontend
public record LoginRequestDTO(String login, String password) {
}