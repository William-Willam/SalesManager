package br.com.salesmanager.desktop.dto;

import java.math.BigDecimal;

public record VendaResponse(Long id, BigDecimal total, String formaPagamento) {
}