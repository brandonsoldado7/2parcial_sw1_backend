package com.example.demo.service;

import com.example.demo.model.Lavadora;
import com.example.demo.repository.LavadoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Lavadora.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class LavadoraService {

    @Autowired
    private LavadoraRepository repository;

    // CREATE o UPDATE
    public Lavadora save(Lavadora entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Lavadora> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Lavadora> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Lavadora por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Lavadora con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
