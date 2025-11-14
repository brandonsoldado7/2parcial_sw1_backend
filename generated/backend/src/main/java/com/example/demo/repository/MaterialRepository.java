package com.example.demo.repository;

import com.example.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Material.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
