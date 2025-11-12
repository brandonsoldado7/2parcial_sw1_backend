package com.segundoparcialsw1.diagramadorinteligente.controller;

import com.segundoparcialsw1.diagramadorinteligente.model.Diagrama;
import com.segundoparcialsw1.diagramadorinteligente.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorService colaboradorService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarColaborador(@RequestBody Map<String, Object> body) {
        try {
            String enlaceInvitacion = (String) body.get("enlace_invitacion");
            Long usuarioId = ((Number) body.get("usuarioId")).longValue();

            Optional<Diagrama> diagramaOpt = colaboradorService.registrarColaborador(enlaceInvitacion, usuarioId);

            if (diagramaOpt.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Enlace de invitación no válido"));
            }

            Diagrama diagrama = diagramaOpt.get();

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Colaborador registrado correctamente",
                    "diagramaId", diagrama.getId(),
                    "diagrama_json", diagrama.getDiagramaJson()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al registrar colaborador: " + e.getMessage()));
        }
    }

    @PostMapping("/desactivar")
    public ResponseEntity<?> desactivarColaboracionesYDiagramas(@RequestBody Map<String, Object> body) {
        try {
            Long usuarioId = ((Number) body.get("usuarioId")).longValue();
            colaboradorService.desactivarColaboracionesYDiagramas(usuarioId);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Se desactivaron todos los diagramas y colaboraciones del usuario con id " + usuarioId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al desactivar colaboraciones: " + e.getMessage()));
        }
    }

    @PostMapping("/obtener-enlace")
    public ResponseEntity<?> obtenerEnlace(@RequestBody Map<String, Object> body) {
        try {
            Object idObj = body.get("id");
            if (idObj == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Falta el id del diagrama"));
            }

            Long id;
            if (idObj instanceof Number) {
                id = ((Number) idObj).longValue();
            } else {
                id = Long.parseLong(idObj.toString());
            }

            Optional<String> enlaceOpt = colaboradorService.obtenerEnlaceInvitacion(id);

            if (enlaceOpt.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(Map.of("error", "Diagrama no encontrado"));
            }

            return ResponseEntity.ok(Map.of("enlace_invitacion", enlaceOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al obtener el enlace de invitación: " + e.getMessage()));
        }
    }

    @PostMapping("/usuarios-activos")
    public ResponseEntity<?> obtenerUsuariosActivos(@RequestBody Map<String, Object> body) {
        try {
            Object idObj = body.get("diagramaId");
            if (idObj == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Falta el diagramaId"));
            }

            Long diagramaId;
            if (idObj instanceof Number) {
                diagramaId = ((Number) idObj).longValue();
            } else {
                diagramaId = Long.parseLong(idObj.toString());
            }

            List<Map<String, Object>> usuarios = colaboradorService.obtenerColorYSiglaUsuarios(diagramaId);
            return ResponseEntity.ok(Map.of("usuarios", usuarios));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al obtener usuarios activos: " + e.getMessage()));
        }
    }
}
