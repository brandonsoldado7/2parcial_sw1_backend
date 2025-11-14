package com.example.demo.repository;

import com.example.demo.model.BaseDeDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad BaseDeDatos.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface BaseDeDatosRepository extends JpaRepository<BaseDeDatos, Long> {
}
