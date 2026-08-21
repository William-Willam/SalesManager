package br.com.salesmanager.desktop.model;

import br.com.salesmanager.desktop.dto.ProdutoResponse;

import java.math.BigDecimal;

public class ItemPedido {

    private final ProdutoResponse produto;
    private int quantidade;

    public ItemPedido(ProdutoResponse produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ProdutoResponse getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void incrementar() {

        quantidade++;
    }

    public BigDecimal getSubtotal() {

        return produto.preco().multiply(BigDecimal.valueOf(quantidade));
    }

    public void decrementar() {
        if (quantidade > 0) {
            quantidade--;
        }
    }

}