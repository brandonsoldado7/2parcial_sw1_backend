package com.example.demo.repository;

import com.example.demo.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Reporte.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
}
