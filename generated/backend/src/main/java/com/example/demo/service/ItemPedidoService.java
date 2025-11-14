package com.example.demo.service;

import com.example.demo.model.ItemPedido;
import com.example.demo.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad ItemPedido.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository repository;

    // CREATE o UPDATE
    public ItemPedido save(ItemPedido entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<ItemPedido> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<ItemPedido> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando ItemPedido por ID: " + e.getMessage());
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
            System.err.println("⚠️ Error eliminando ItemPedido con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
