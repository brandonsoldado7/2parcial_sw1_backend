package com.example.demo.repository;

import com.example.demo.model.Figura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Figura.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface FiguraRepository extends JpaRepository<Figura, Long> {
}
