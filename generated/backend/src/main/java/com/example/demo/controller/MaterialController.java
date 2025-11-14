package com.example.demo.controller;

import com.example.demo.model.Material;
import com.example.demo.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad Material.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/material")
@CrossOrigin(origins = "*")
public class MaterialController {

    @Autowired
    private MaterialService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<Material> guardar(
            @RequestBody Material data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Material>> listar() {
        List<Material> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Material> obtenerPorId(@PathVariable Long id) {
        Optional<Material> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Material> actualizar(
            @PathVariable Long id,
            @RequestBody Material data) {

        Material entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        Material updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
