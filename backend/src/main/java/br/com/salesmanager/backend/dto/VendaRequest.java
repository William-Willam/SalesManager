package br.com.salesmanager.backend.dto;

import br.com.salesmanager.backend.model.FormaPagamento;
import java.util.List;

public record VendaRequest(FormaPagamento formaPagamento, List<ItemVendaRequest> itens) {
}