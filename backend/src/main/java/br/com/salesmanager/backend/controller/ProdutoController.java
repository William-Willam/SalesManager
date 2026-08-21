package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.model.Produto;
import br.com.salesmanager.backend.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public Page<Produto> listar(
            @RequestParam(required = false) Long categoriaId,
            @org.springframework.data.web.PageableDefault(size = 20) Pageable pageable) {
        if (categoriaId != null) {
            return produtoService.listarPorCategoria(categoriaId, pageable);
        }
        return produtoService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Produto> criar(@jakarta.validation.Valid @RequestBody Produto produto) {
        Produto salvo = produtoService.salvar(produto);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @jakarta.validation.Valid @RequestBody Produto produto) {
        produto.setId(id);
        return ResponseEntity.ok(produtoService.salvar(produto));
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/imagem")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Produto> enviarImagem(
            @PathVariable Long id,
            @RequestParam("arquivo") org.springframework.web.multipart.MultipartFile arquivo) throws java.io.IOException {

        Produto produto = produtoService.buscarPorId(id);

        String extensao = org.springframework.util.StringUtils.getFilenameExtension(arquivo.getOriginalFilename());
        String nomeArquivo = java.util.UUID.randomUUID() + "." + extensao;

        java.nio.file.Path pasta = java.nio.file.Paths.get("uploads", "produtos").toAbsolutePath();
        java.nio.file.Files.createDirectories(pasta);

        java.nio.file.Path destino = pasta.resolve(nomeArquivo);
        arquivo.transferTo(destino);

        produto.setImagemUrl("/uploads/produtos/" + nomeArquivo);
        Produto salvo = produtoService.salvar(produto);

        return ResponseEntity.ok(salvo);
    }
}