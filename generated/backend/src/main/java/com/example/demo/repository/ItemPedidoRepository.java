package com.example.demo.repository;

import com.example.demo.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad ItemPedido.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
