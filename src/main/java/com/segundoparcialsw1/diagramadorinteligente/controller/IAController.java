package com.segundoparcialsw1.diagramadorinteligente.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.segundoparcialsw1.diagramadorinteligente.service.IAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/ia")
@RequiredArgsConstructor
@Slf4j
public class IAController {

    private final IAService iaService;

    // 🔹 Generar diagrama desde prompt
    @PostMapping("/generar")
    public ResponseEntity<?> generarDiagrama(@RequestBody Map<String, Object> requestBody) {
        try {
            String prompt = (String) requestBody.get("prompt");
            JsonNode modeloGenerado = iaService.generarDiagrama(prompt);
            return ResponseEntity.ok(Map.of("modeloGenerado", modeloGenerado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al generar diagrama", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "mensaje", "Error interno al generar diagrama",
                    "detalle", e.getMessage()
            ));
        }
    }

    // 🔹 Modificar diagrama existente
    @PostMapping("/modificar")
    public ResponseEntity<?> modificarDiagrama(@RequestBody Map<String, Object> requestBody) {
        try {
            String prompt = (String) requestBody.get("prompt");
            Object jsonActual = requestBody.get("jsonActual");
            JsonNode modeloGenerado = iaService.modificarDiagrama(prompt, jsonActual);
            return ResponseEntity.ok(Map.of("modeloGenerado", modeloGenerado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al modificar diagrama", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "mensaje", "Error interno al modificar diagrama",
                    "detalle", e.getMessage()
            ));
        }
    }
@PostMapping("/imagenBase64")
public ResponseEntity<?> generarDesdeImagenBase64(@RequestBody Map<String, String> requestBody) {
    try {
        String base64Image = requestBody.get("base64Image");
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El campo 'base64Image' es obligatorio."));
        }

        JsonNode modeloGenerado = iaService.generarDesdeImagenBase64(base64Image);
        return ResponseEntity.ok(Map.of("modeloGenerado", modeloGenerado));
    } catch (Exception e) {
        log.error("Error al generar diagrama desde imagen base64", e);
        return ResponseEntity.internalServerError().body(Map.of(
                "mensaje", "Error interno al generar diagrama desde imagen base64",
                "detalle", e.getMessage()
        ));
    }
}

    // 🔹 Generar diagrama desde imagen UML
    @PostMapping(value = "/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generarDesdeImagen(@RequestParam("file") MultipartFile file) {
        try {
            JsonNode modeloGenerado = iaService.generarDesdeImagen(file);
            return ResponseEntity.ok(Map.of("modeloGenerado", modeloGenerado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al generar diagrama desde imagen", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "mensaje", "Error interno al generar diagrama desde imagen",
                    "detalle", e.getMessage()
            ));
        }
    }
}
