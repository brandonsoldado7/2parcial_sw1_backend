package com.example.demo.controller;

import com.example.demo.model.Electrodomestico;
import com.example.demo.service.ElectrodomesticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad Electrodomestico.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/electrodomestico")
@CrossOrigin(origins = "*")
public class ElectrodomesticoController {

    @Autowired
    private ElectrodomesticoService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<Electrodomestico> guardar(
            @RequestBody Electrodomestico data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Electrodomestico>> listar() {
        List<Electrodomestico> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Electrodomestico> obtenerPorId(@PathVariable Long id) {
        Optional<Electrodomestico> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Electrodomestico> actualizar(
            @PathVariable Long id,
            @RequestBody Electrodomestico data) {

        Electrodomestico entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        Electrodomestico updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
