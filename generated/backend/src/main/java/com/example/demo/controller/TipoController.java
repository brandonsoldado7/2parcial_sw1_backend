package com.example.demo.controller;

import com.example.demo.model.Tipo;
import com.example.demo.service.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad Tipo.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/tipo")
@CrossOrigin(origins = "*")
public class TipoController {

    @Autowired
    private TipoService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<Tipo> guardar(
            @RequestBody Tipo data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Tipo>> listar() {
        List<Tipo> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Tipo> obtenerPorId(@PathVariable Long id) {
        Optional<Tipo> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Tipo> actualizar(
            @PathVariable Long id,
            @RequestBody Tipo data) {

        Tipo entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        Tipo updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
