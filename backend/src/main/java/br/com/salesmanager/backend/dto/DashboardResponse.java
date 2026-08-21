package br.com.salesmanager.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        BigDecimal totalVendas,
        Long quantidadeVendas,
        List<VendaPorDiaResponse> vendasPorDia,
        List<ProdutoMaisVendidoResponse> produtosMaisVendidos
) {
}