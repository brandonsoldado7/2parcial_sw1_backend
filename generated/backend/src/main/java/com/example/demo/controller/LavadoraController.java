package com.example.demo.controller;

import com.example.demo.model.Lavadora;
import com.example.demo.service.LavadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad Lavadora.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/lavadora")
@CrossOrigin(origins = "*")
public class LavadoraController {

    @Autowired
    private LavadoraService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<Lavadora> guardar(
            @RequestBody Lavadora data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Lavadora>> listar() {
        List<Lavadora> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Lavadora> obtenerPorId(@PathVariable Long id) {
        Optional<Lavadora> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Lavadora> actualizar(
            @PathVariable Long id,
            @RequestBody Lavadora data) {

        Lavadora entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        Lavadora updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
