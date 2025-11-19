package com.dsw.comunideskback.service;

import com.dsw.comunideskback.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostExpirationService {

    // Usar um Logger é uma boa prática para ver o que o robô está fazendo
    private static final Logger logger = LoggerFactory.getLogger(PostExpirationService.class);

    @Autowired
    private PostRepository postRepository;

    private static final int DIAS_PARA_EXPIRAR = 7;

    /**
     * Este método será executado automaticamente.
     * A expressão "cron" abaixo significa: "Às 2:00 da manhã, todos os dias".
     * (Segundo Minuto Hora Dia-do-Mês Mês Dia-da-Semana)
     */
    @Scheduled(cron = "0 0 2 * * *") 
    public void expirarPostsAntigos() {
        logger.info("Iniciando tarefa agendada: Expirando posts antigos...");
        
        // 1. Calcula a data limite (7 dias atrás)
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(DIAS_PARA_EXPIRAR);
        
        // 2. Chama o método do repositório para fazer o update em massa
        try {
            postRepository.desativarPostsAntigos(dataLimite);
            logger.info("Tarefa concluída. Posts com data anterior a {} foram desativados.", dataLimite);
        } catch (Exception e) {
            logger.error("Erro ao executar a tarefa de expiração de posts.", e);
        }
    }
}