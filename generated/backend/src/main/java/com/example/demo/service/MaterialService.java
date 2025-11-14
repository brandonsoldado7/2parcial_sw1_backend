package com.example.demo.service;

import com.example.demo.model.Material;
import com.example.demo.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Material.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    // CREATE o UPDATE
    public Material save(Material entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Material> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Material> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Material por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // DELETE
    public boolean delete(Long id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("⚠️ Error eliminando Material con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
