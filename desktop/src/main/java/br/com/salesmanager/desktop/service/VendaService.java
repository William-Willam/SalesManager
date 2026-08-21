package br.com.salesmanager.desktop.service;

import br.com.salesmanager.desktop.dto.ItemVendaRequest;
import br.com.salesmanager.desktop.dto.VendaRequest;
import br.com.salesmanager.desktop.dto.VendaResponse;
import br.com.salesmanager.desktop.http.ApiClient;
import br.com.salesmanager.desktop.model.ItemPedido;

import java.util.List;

public class VendaService {

    private final ApiClient apiClient = new ApiClient();

    public VendaResponse registrar(List<ItemPedido> carrinho, String formaPagamento) {
        List<ItemVendaRequest> itens = carrinho.stream()
                .map(item -> new ItemVendaRequest(item.getProduto().id(), item.getQuantidade()))
                .toList();

        return apiClient.post("/vendas", new VendaRequest(formaPagamento, itens), VendaResponse.class);
    }
}