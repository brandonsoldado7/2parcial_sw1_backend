package com.segundoparcialsw1.diagramadorinteligente.controller;

import com.segundoparcialsw1.diagramadorinteligente.model.Diagrama;
import com.segundoparcialsw1.diagramadorinteligente.service.DiagramaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/diagramas")
public class DiagramaController {

    private final DiagramaService diagramaService;

    public DiagramaController(DiagramaService diagramaService) {
        this.diagramaService = diagramaService;
    }

    public static class CrearDiagramaRequest {
        @NotNull(message = "El nombre es obligatorio")
        private String nombre;

        @NotNull(message = "El usuarioId es obligatorio")
        private Long usuarioId;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    }

    public static class UsuarioIdRequest {
        @NotNull(message = "El usuarioId es obligatorio")
        private Long usuarioId;

        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarDiagrama(@RequestBody Map<String, Object> request) {
        try {
            Long id = request.get("id") != null ? Long.valueOf(request.get("id").toString()) : null;
            String diagramaJson = (String) request.get("diagrama_json");

            if (id == null || diagramaJson == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Faltan datos requeridos: id y diagrama_json"));
            }

            var resultado = diagramaService.guardarDiagrama(id, diagramaJson);

            if (resultado.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Diagrama no encontrado"));
            }

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Diagrama guardado correctamente",
                    "diagrama", resultado.get()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al guardar el diagrama: " + e.getMessage()));
        }
    }

    @PostMapping("/obtener_por_id")
    public ResponseEntity<?> obtenerDiagramaPorId(@RequestBody Map<String, Long> request) {
        try {
            Long id = request.get("id");
            if (id == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Falta el id del diagrama"));
            }

            var resultado = diagramaService.obtenerDiagramaPorId(id);

            if (resultado.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Diagrama no encontrado"));
            }

            return ResponseEntity.ok(Map.of("diagrama", resultado.get()));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al obtener el diagrama: " + e.getMessage()));
        }
    }

    @PostMapping("/obtener_por_usuario")
    public ResponseEntity<?> obtenerDiagramasPorUsuario(@Valid @RequestBody UsuarioIdRequest request) {
        try {
            Long usuarioId = request.getUsuarioId();
            var diagramas = diagramaService.obtenerDiagramasPorUsuario(usuarioId);

            return ResponseEntity.ok(Map.of("diagramas", diagramas));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al obtener los diagramas: " + e.getMessage()));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearDiagrama(@Valid @RequestBody CrearDiagramaRequest request) {
        try {
            Diagrama diagrama = diagramaService.crearDiagrama(request.getNombre(), request.getUsuarioId());

            if (diagrama == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Existe un diagrama con ese nombre. Por favor, elige otro nombre."));
            }

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Diagrama creado correctamente",
                    "diagrama", Map.of(
                            "id", diagrama.getId(),
                            "diagramaJson", diagrama.getDiagramaJson(),
                            "enlaceInvitacion", diagrama.getEnlaceInvitacion()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al crear el diagrama: " + e.getMessage()));
        }
    }
}
