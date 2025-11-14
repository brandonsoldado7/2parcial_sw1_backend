package com.example.demo.controller;

import com.example.demo.model.DependenciaTarea;
import com.example.demo.service.DependenciaTareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad DependenciaTarea.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/dependenciatarea")
@CrossOrigin(origins = "*")
public class DependenciaTareaController {

    @Autowired
    private DependenciaTareaService service;


    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<DependenciaTarea> guardar(
            @RequestBody DependenciaTarea data) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<DependenciaTarea>> listar() {
        List<DependenciaTarea> list = service.findAll();

        return ResponseEntity.ok(list);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DependenciaTarea> obtenerPorId(@PathVariable Long id) {
        Optional<DependenciaTarea> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(data.get());
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DependenciaTarea> actualizar(
            @PathVariable Long id,
            @RequestBody DependenciaTarea data) {

        DependenciaTarea entity = data;

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        DependenciaTarea updated = service.save(entity);

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
