package br.com.salesmanager.backend.repository;

import br.com.salesmanager.backend.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
}