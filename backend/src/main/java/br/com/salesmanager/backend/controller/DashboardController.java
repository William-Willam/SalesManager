package br.com.salesmanager.backend.controller;

import br.com.salesmanager.backend.dto.DashboardResponse;
import br.com.salesmanager.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('GERENTE')")
    public DashboardResponse obter(@RequestParam(defaultValue = "30") int dias) {
        return dashboardService.obterEstatisticas(dias);
    }
}