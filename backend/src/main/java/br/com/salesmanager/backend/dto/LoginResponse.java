package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.Papel;

public record LoginResponse(
        String token,
        String nome,
        String email,
        Papel papel
) {
}