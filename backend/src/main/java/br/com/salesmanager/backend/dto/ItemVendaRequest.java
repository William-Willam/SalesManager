package br.com.salesmanager.backend.dto;

public record ItemVendaRequest(
        Long produtoId,
        Integer quantidade) {
}