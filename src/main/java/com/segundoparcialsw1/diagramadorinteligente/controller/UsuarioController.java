package com.segundoparcialsw1.diagramadorinteligente.controller;

import com.segundoparcialsw1.diagramadorinteligente.model.Usuario;
import com.segundoparcialsw1.diagramadorinteligente.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(final UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody final Usuario usuario) {
        try {
            usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("mensaje", "Error interno del servidor"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody final Map<String, String> datos) {
        String correo = datos.get("correo");
        String password = datos.get("password");

        Map<String, Object> response = usuarioService.login(correo, password);
        String mensaje = (String) response.get("mensaje");

        if ("Correo no existe".equals(mensaje) || "Contraseña incorrecta".equals(mensaje)) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody final Map<String, String> datos) {
        String correo = datos.get("correo");
        Map<String, String> response = usuarioService.resetPasswordPorCorreo(correo);

        if (response.get("mensaje").contains("no está registrado")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
