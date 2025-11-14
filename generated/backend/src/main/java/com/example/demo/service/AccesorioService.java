package com.example.demo.service;

import com.example.demo.model.Accesorio;
import com.example.demo.repository.AccesorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Accesorio.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class AccesorioService {

    @Autowired
    private AccesorioRepository repository;

    // CREATE o UPDATE
    public Accesorio save(Accesorio entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Accesorio> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Accesorio> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Accesorio por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Accesorio con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
