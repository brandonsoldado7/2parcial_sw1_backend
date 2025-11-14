package com.example.demo.service;

import com.example.demo.model.Figura;
import com.example.demo.repository.FiguraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Figura.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class FiguraService {

    @Autowired
    private FiguraRepository repository;

    // CREATE o UPDATE
    public Figura save(Figura entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Figura> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Figura> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Figura por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Figura con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
