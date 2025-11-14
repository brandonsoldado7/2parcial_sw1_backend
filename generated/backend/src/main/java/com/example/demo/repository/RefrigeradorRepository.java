package com.example.demo.repository;

import com.example.demo.model.Refrigerador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Refrigerador.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface RefrigeradorRepository extends JpaRepository<Refrigerador, Long> {
}
