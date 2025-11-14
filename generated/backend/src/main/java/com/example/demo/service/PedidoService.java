package com.example.demo.service;

import com.example.demo.model.Pedido;
import com.example.demo.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad Pedido.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    // CREATE o UPDATE
    public Pedido save(Pedido entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<Pedido> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<Pedido> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando Pedido por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando Pedido con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
