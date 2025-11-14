package com.example.demo.service;

import com.example.demo.model.Reporte;
import com.example.demo.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Reporte.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class ReporteService {

    @Autowired
    private ReporteRepository repository;

    // CREATE o UPDATE
    public Reporte save(Reporte entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Reporte> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Reporte> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Reporte por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Reporte con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
