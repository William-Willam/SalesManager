package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.FormaPagamento;
import br.com.salesmanager.backend.model.StatusVenda;
import br.com.salesmanager.backend.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponse(
        Long id,
        LocalDateTime data,
        BigDecimal total,
        FormaPagamento formaPagamento,
        StatusVenda status,
        String atendente,
        List<ItemVendaResponse> itens
) {
    public static VendaResponse de(Venda venda) {
        List<ItemVendaResponse> itens = venda.getItens().stream()
                .map(ItemVendaResponse::de)
                .toList();

        return new VendaResponse(
                venda.getId(),
                venda.getData(),
                venda.getTotal(),
                venda.getFormaPagamento(),
                venda.getStatus(),
                venda.getUsuario().getNome(),
                itens
        );
    }
}