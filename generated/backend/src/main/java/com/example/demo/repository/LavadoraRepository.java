package com.example.demo.repository;

import com.example.demo.model.Lavadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Lavadora.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface LavadoraRepository extends JpaRepository<Lavadora, Long> {
}
