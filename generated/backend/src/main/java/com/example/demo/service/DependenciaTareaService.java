package com.example.demo.service;

import com.example.demo.model.DependenciaTarea;
import com.example.demo.repository.DependenciaTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad DependenciaTarea.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class DependenciaTareaService {

    @Autowired
    private DependenciaTareaRepository repository;

    // CREATE o UPDATE
    public DependenciaTarea save(DependenciaTarea entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<DependenciaTarea> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<DependenciaTarea> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando DependenciaTarea por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando DependenciaTarea con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
