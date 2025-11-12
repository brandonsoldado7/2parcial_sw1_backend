package com.segundoparcialsw1.diagramadorinteligente.service;

import com.segundoparcialsw1.diagramadorinteligente.model.Usuario;
import com.segundoparcialsw1.diagramadorinteligente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static final String[] COLORES = {
            "#cc630d", "#3c65eb", "#28a745", "#d63384", "#fd7e14",
            "#6f42c1", "#20c997", "#e83e8c", "#0dcaf0", "#ffc107",
            "#FF0000", "#DC143C", "#FF1493", "#FFC0CB", "#FA8072",
            "#FFA500", "#FFD700", "#FFFF00", "#DAA520", "#FF4500",
            "#008000", "#7CFC00", "#3CB371", "#006400", "#90EE90",
            "#0000FF", "#1E90FF", "#4169E1", "#87CEEB", "#00BFFF",
            "#800080", "#9370DB", "#4B0082", "#DDA0DD", "#EE82EE",
            "#A52A2A", "#D2B48C", "#CD853F", "#808000", "#5F9EA0",
            "#000000", "#808080", "#A9A9A9", "#D3D3D3", "#C0C0C0",
            "#00FFFF", "#FF00FF", "#7FFFD4", "#00FF00", "#6A5ACD"
    };

    public Map<String, String> resetPasswordPorCorreo(String correo) {
        Map<String, String> response = new HashMap<>();
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            response.put("mensaje", "El correo no está registrado ❌");
            return response;
        }
        String nuevaPassword = generarPasswordSegura();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        enviarCorreoRecuperacion(usuario.getCorreo(), nuevaPassword);
        response.put("mensaje", "Nueva contraseña enviada al correo ✅");
        return response;
    }

    public Map<String, Object> login(String correo, String password) {
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            response.put("mensaje", "Correo no existe");
            return response;
        }
        if (!verificarPassword(password, usuario.getPassword())) {
            response.put("mensaje", "Contraseña incorrecta");
            return response;
        }
        response.put("mensaje", "Login exitoso");
        response.put("id", usuario.getId());
        response.put("nombreCompleto", usuario.getNombre() + " " +
                usuario.getApellidoP() + " " + usuario.getApellidoM());
        return response;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getColor() == null || usuario.getColor().isEmpty()) {
            int index = (int) (Math.random() * COLORES.length);
            usuario.setColor(COLORES[index]);
        }
        return usuarioRepository.save(usuario);
    }

    public boolean verificarPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generarPasswordSegura() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void enviarCorreoRecuperacion(String destinatario, String nuevaPassword) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("brandonsoldado7@gmail.com");
            mensaje.setTo(destinatario);
            mensaje.setSubject("Recuperación de contraseña - Diagramador Inteligente");
            mensaje.setText(
                    "Hola 👋,\n\nTu nueva contraseña es: " + nuevaPassword +
                            "\n\nPor seguridad, cámbiala al iniciar sesión.\n\nSaludos,\nEquipo Diagramador Inteligente."
            );
            mailSender.send(mensaje);
            log.info("Correo enviado correctamente a {}", destinatario);
        } catch (Exception e) {
            log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage(), e);
        }
    }
}
