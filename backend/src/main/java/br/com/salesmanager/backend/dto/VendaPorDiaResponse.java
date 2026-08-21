package br.com.salesmanager.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VendaPorDiaResponse(LocalDate data, BigDecimal total) {
}