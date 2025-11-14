package com.example.demo.repository;

import com.example.demo.model.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Tipo.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
