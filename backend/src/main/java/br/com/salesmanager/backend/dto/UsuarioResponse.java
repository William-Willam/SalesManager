package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.Papel;
import br.com.salesmanager.backend.model.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Papel papel,
        Boolean ativo
) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel(),
                usuario.getAtivo()
        );
    }
}