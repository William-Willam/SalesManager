package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.dto.VendaRequest;
import br.com.salesmanager.backend.dto.VendaResponse;
import br.com.salesmanager.backend.model.Venda;
import br.com.salesmanager.backend.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ATENDENTE')")
    public ResponseEntity<VendaResponse> registrar(@RequestBody VendaRequest request) {
        VendaResponse venda = vendaService.registrar(request);
        return ResponseEntity.status(201).body(venda);
    }

    @GetMapping
    public Page<VendaResponse> listar(@org.springframework.data.web.PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable) {
        return vendaService.listarTodas(pageable).map(VendaResponse::de);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(VendaResponse.de(vendaService.buscarPorId(id)));
    }
}