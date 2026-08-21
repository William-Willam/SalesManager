package br.com.salesmanager.backend.service;

import br.com.salesmanager.backend.model.Produto;
import br.com.salesmanager.backend.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Page<Produto> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Page<Produto> listarPorCategoria(Long categoriaId, Pageable pageable) {
        return produtoRepository.findByCategoriaId(categoriaId, pageable);
    }
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new br.com.salesmanager.backend.exception.RecursoNaoEncontradoException(
                        "Produto não encontrado com id: " + id));
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        buscarPorId(id); // já lança RecursoNaoEncontradoException se não existir
        produtoRepository.deleteById(id);
    }
}