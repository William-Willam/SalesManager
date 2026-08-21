package br.com.salesmanager.backend.service;

import br.com.salesmanager.backend.exception.RecursoNaoEncontradoException;
import br.com.salesmanager.backend.model.Categoria;
import br.com.salesmanager.backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Page<Categoria> listarTodas(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id: " + id));
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void excluir(Long id) {
        buscarPorId(id);
        categoriaRepository.deleteById(id);
    }
}