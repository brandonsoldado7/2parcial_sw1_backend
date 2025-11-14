package com.example.demo.service;

import com.example.demo.model.Circulo;
import com.example.demo.repository.CirculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Circulo.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class CirculoService {

    @Autowired
    private CirculoRepository repository;

    // CREATE o UPDATE
    public Circulo save(Circulo entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Circulo> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Circulo> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Circulo por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Circulo con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
