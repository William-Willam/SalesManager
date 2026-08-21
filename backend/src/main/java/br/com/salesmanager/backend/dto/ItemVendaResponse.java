package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.ItemVenda;

import java.math.BigDecimal;

public record ItemVendaResponse(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemVendaResponse de(ItemVenda item) {
        return new ItemVendaResponse(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}