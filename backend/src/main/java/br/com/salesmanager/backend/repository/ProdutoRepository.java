package br.com.salesmanager.backend.repository;

import br.com.salesmanager.backend.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByCategoriaId(Long categoriaId, Pageable pageable);
}