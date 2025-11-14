package com.example.demo.service;

import com.example.demo.model.Electrodomestico;
import com.example.demo.repository.ElectrodomesticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Electrodomestico.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class ElectrodomesticoService {

    @Autowired
    private ElectrodomesticoRepository repository;

    // CREATE o UPDATE
    public Electrodomestico save(Electrodomestico entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Electrodomestico> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Electrodomestico> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Electrodomestico por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Electrodomestico con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
