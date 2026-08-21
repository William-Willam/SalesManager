package br.com.salesmanager.backend.service;

import br.com.salesmanager.backend.dto.ItemVendaRequest;
import br.com.salesmanager.backend.dto.VendaRequest;
import br.com.salesmanager.backend.dto.VendaResponse;
import br.com.salesmanager.backend.exception.RecursoNaoEncontradoException;
import br.com.salesmanager.backend.exception.RegraNegocioException;
import br.com.salesmanager.backend.model.*;
import br.com.salesmanager.backend.repository.ProdutoRepository;
import br.com.salesmanager.backend.repository.UsuarioRepository;
import br.com.salesmanager.backend.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public VendaResponse registrar(VendaRequest request) {
        if (request.itens() == null || request.itens().isEmpty()) {
            throw new RegraNegocioException("A venda precisa ter pelo menos um item");
        }

        String emailAutenticado = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário autenticado não encontrado"));

        Venda venda = new Venda(usuario, request.formaPagamento());
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVendaRequest itemRequest : request.itens()) {
            if (itemRequest.quantidade() == null || itemRequest.quantidade() <= 0) {
                throw new RegraNegocioException("Quantidade inválida para o produto " + itemRequest.produtoId());
            }

            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Produto não encontrado: " + itemRequest.produtoId()));

            ItemVenda item = new ItemVenda(venda, produto, itemRequest.quantidade(), produto.getPreco());
            venda.getItens().add(item);
            total = total.add(item.getSubtotal());
        }

        venda.setTotal(total);
        return VendaResponse.de(vendaRepository.save(venda));
    }

    public Page<Venda> listarTodas(Pageable pageable) {
        return vendaRepository.findAll(pageable);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada: " + id));
    }
}