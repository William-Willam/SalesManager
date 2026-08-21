package br.com.salesmanager.desktop.service;

import br.com.salesmanager.desktop.dto.CategoriaResponse;
import br.com.salesmanager.desktop.dto.PageResponse;
import br.com.salesmanager.desktop.dto.ProdutoResponse;
import br.com.salesmanager.desktop.http.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ProdutoService {

    private final ApiClient apiClient = new ApiClient();

    public List<CategoriaResponse> listarCategorias() {
        PageResponse<CategoriaResponse> pagina = apiClient.get(
                "/categorias?size=200",
                new TypeReference<PageResponse<CategoriaResponse>>() {});
        return pagina.content();
    }

    public List<ProdutoResponse> listarProdutosPorCategoria(Long categoriaId) {
        PageResponse<ProdutoResponse> pagina = apiClient.get(
                "/produtos?categoriaId=" + categoriaId + "&size=200",
                new TypeReference<PageResponse<ProdutoResponse>>() {});

        return pagina.content().stream()
                .filter(produto -> Boolean.TRUE.equals(produto.ativo()))
                .toList();
    }
}