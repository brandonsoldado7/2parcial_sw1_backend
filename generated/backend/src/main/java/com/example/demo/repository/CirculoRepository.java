package com.example.demo.repository;

import com.example.demo.model.Circulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Circulo.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface CirculoRepository extends JpaRepository<Circulo, Long> {
}
