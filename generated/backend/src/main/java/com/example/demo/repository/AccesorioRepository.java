package com.example.demo.repository;

import com.example.demo.model.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Accesorio.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {
}
