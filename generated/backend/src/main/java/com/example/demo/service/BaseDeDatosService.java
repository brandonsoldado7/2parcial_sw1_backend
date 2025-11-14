package com.example.demo.service;

import com.example.demo.model.BaseDeDatos;
import com.example.demo.repository.BaseDeDatosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad BaseDeDatos.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class BaseDeDatosService {

    @Autowired
    private BaseDeDatosRepository repository;

    // CREATE o UPDATE
    public BaseDeDatos save(BaseDeDatos entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<BaseDeDatos> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<BaseDeDatos> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando BaseDeDatos por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando BaseDeDatos con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
