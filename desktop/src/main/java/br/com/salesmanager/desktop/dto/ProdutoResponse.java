package br.com.salesmanager.desktop.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        CategoriaResponse categoria,
        Boolean ativo,
        String imagemUrl
) {
}