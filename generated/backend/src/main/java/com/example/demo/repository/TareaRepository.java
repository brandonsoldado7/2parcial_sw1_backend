package com.example.demo.repository;

import com.example.demo.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Tarea.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
