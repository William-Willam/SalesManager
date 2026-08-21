package br.com.salesmanager.backend.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        List<String> detalhes
) {
}