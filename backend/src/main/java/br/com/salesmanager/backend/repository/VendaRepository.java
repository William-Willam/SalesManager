package br.com.salesmanager.backend.repository;

import br.com.salesmanager.backend.dto.ProdutoMaisVendidoResponse;
import br.com.salesmanager.backend.dto.VendaPorDiaResponse;
import br.com.salesmanager.backend.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venda v " +
            "WHERE v.status = 'FINALIZADA' AND v.data >= :desde")
    BigDecimal somarTotalDesde(@Param("desde") LocalDateTime desde);

    @Query("SELECT COUNT(v) FROM Venda v WHERE v.status = 'FINALIZADA' AND v.data >= :desde")
    Long contarVendasDesde(@Param("desde") LocalDateTime desde);

    @Query("SELECT FUNCTION('DATE', v.data), COALESCE(SUM(v.total), 0) " +
            "FROM Venda v " +
            "WHERE v.status = 'FINALIZADA' AND v.data >= :desde " +
            "GROUP BY FUNCTION('DATE', v.data) " +
            "ORDER BY FUNCTION('DATE', v.data)")
    List<Object[]> agruparVendasPorDiaRaw(@Param("desde") LocalDateTime desde);

    @Query("SELECT new br.com.salesmanager.backend.dto.ProdutoMaisVendidoResponse(" +
            "iv.produto.nome, SUM(iv.quantidade)) " +
            "FROM ItemVenda iv " +
            "WHERE iv.venda.status = 'FINALIZADA' AND iv.venda.data >= :desde " +
            "GROUP BY iv.produto.nome " +
            "ORDER BY SUM(iv.quantidade) DESC")
    List<ProdutoMaisVendidoResponse> listarProdutosMaisVendidos(@Param("desde") LocalDateTime desde);
}