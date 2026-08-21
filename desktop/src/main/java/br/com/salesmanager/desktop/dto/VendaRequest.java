package br.com.salesmanager.desktop.dto;

import java.util.List;

public record VendaRequest(String formaPagamento, List<ItemVendaRequest> itens) {
}