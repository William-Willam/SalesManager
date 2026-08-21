package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.model.Categoria;
import br.com.salesmanager.backend.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public Page<Categoria> listar(@PageableDefault(size = 20) Pageable pageable) {
        return categoriaService.listarTodas(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria) {
        Categoria salva = categoriaService.salvar(categoria);
        return ResponseEntity.status(201).body(salva);
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        categoria.setId(id);
        return ResponseEntity.ok(categoriaService.salvar(categoria));
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}