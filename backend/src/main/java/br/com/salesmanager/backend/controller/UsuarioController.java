package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.dto.UsuarioRequest;
import br.com.salesmanager.backend.dto.UsuarioResponse;
import br.com.salesmanager.backend.dto.UsuarioUpdateRequest;
import br.com.salesmanager.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public Page<UsuarioResponse> listar(@PageableDefault(size = 20) Pageable pageable) {
        return usuarioService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(201).body(usuarioService.criar(request));
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}