package com.example.demo.repository;

import com.example.demo.model.DependenciaTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad DependenciaTarea.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface DependenciaTareaRepository extends JpaRepository<DependenciaTarea, Long> {
}
