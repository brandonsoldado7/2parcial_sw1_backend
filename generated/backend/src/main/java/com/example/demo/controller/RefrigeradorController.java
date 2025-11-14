package com.example.demo.controller;

import com.example.demo.model.Refrigerador;
import com.example.demo.service.RefrigeradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad Refrigerador.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/refrigerador")
@CrossOrigin(origins = "*")
public class RefrigeradorController {

    @Autowired
    private RefrigeradorService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<Refrigerador> guardar(
            @RequestBody Refrigerador data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Refrigerador>> listar() {
        List<Refrigerador> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Refrigerador> obtenerPorId(@PathVariable Long id) {
        Optional<Refrigerador> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Refrigerador> actualizar(
            @PathVariable Long id,
            @RequestBody Refrigerador data) {

        Refrigerador entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        Refrigerador updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
