package br.com.salesmanager.backend.service;

import br.com.salesmanager.backend.dto.DashboardResponse;
import br.com.salesmanager.backend.dto.VendaPorDiaResponse;
import br.com.salesmanager.backend.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DashboardService {

    @Autowired
    private VendaRepository vendaRepository;

    public DashboardResponse obterEstatisticas(int diasAtras) {
        LocalDateTime desde = LocalDateTime.now().minusDays(diasAtras);

        var totalVendas = vendaRepository.somarTotalDesde(desde);
        var quantidadeVendas = vendaRepository.contarVendasDesde(desde);

        var vendasPorDia = vendaRepository.agruparVendasPorDiaRaw(desde).stream()
                .map(this::mapearVendaPorDia)
                .toList();

        var produtosMaisVendidos = vendaRepository
                .listarProdutosMaisVendidos(desde)
                .stream()
                .limit(5)
                .toList();

        return new DashboardResponse(totalVendas, quantidadeVendas, vendasPorDia, produtosMaisVendidos);
    }

    private VendaPorDiaResponse mapearVendaPorDia(Object[] linha) {
        LocalDate data = ((java.sql.Date) linha[0]).toLocalDate();
        BigDecimal total = (BigDecimal) linha[1];
        return new VendaPorDiaResponse(data, total);
    }
}