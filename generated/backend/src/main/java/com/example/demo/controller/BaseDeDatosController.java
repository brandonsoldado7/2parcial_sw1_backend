package com.example.demo.controller;

import com.example.demo.model.BaseDeDatos;
import com.example.demo.service.BaseDeDatosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad BaseDeDatos.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/basededatos")
@CrossOrigin(origins = "*")
public class BaseDeDatosController {

    @Autowired
    private BaseDeDatosService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<BaseDeDatos> guardar(
            @RequestBody BaseDeDatos data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<BaseDeDatos>> listar() {
        List<BaseDeDatos> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseDeDatos> obtenerPorId(@PathVariable Long id) {
        Optional<BaseDeDatos> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<BaseDeDatos> actualizar(
            @PathVariable Long id,
            @RequestBody BaseDeDatos data) {

        BaseDeDatos entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        BaseDeDatos updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
