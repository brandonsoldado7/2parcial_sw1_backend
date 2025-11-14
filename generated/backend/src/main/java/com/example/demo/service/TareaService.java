package com.example.demo.service;

import com.example.demo.model.Tarea;
import com.example.demo.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Tarea.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class TareaService {

    @Autowired
    private TareaRepository repository;

    // CREATE o UPDATE
    public Tarea save(Tarea entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Tarea> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Tarea> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Tarea por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Tarea con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
