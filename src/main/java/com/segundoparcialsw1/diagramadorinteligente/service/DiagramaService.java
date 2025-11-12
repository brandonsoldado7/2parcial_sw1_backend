package com.segundoparcialsw1.diagramadorinteligente.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segundoparcialsw1.diagramadorinteligente.model.Diagrama;
import com.segundoparcialsw1.diagramadorinteligente.model.Usuario;
import com.segundoparcialsw1.diagramadorinteligente.repository.ColaboradorRepository;
import com.segundoparcialsw1.diagramadorinteligente.repository.DiagramaRepository;
import com.segundoparcialsw1.diagramadorinteligente.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiagramaService {

    private final DiagramaRepository diagramaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ObjectMapper objectMapper;

    public DiagramaService(
            DiagramaRepository diagramaRepository,
            UsuarioRepository usuarioRepository,
            ColaboradorRepository colaboradorRepository,
            ObjectMapper objectMapper
    ) {
        this.diagramaRepository = diagramaRepository;
        this.usuarioRepository = usuarioRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Optional<Map<String, Object>> guardarDiagrama(Long id, String diagramaJson) {
        return diagramaRepository.findById(id).map(diagrama -> {
            diagrama.setDiagramaJson(diagramaJson);
            diagramaRepository.save(diagrama);

            Object diagramaJsonObj;
            try {
                diagramaJsonObj = objectMapper.readValue(diagramaJson, Map.class);
            } catch (JsonProcessingException e) {
                diagramaJsonObj = new HashMap<>();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", diagrama.getId());
            response.put("diagrama_json", diagramaJsonObj);
            return response;
        });
    }

    @Transactional
    public Optional<Map<String, Object>> obtenerDiagramaPorId(Long id) {
        return diagramaRepository.findById(id).map(diagrama -> {
            Long usuarioId = diagrama.getUsuario().getId();

            diagramaRepository.desactivarOtrosDiagramas(usuarioId, id);
            colaboradorRepository.desactivarPorUsuario(usuarioId);

            if (!Boolean.TRUE.equals(diagrama.getUsuarioCreadorActivo())) {
                diagrama.setUsuarioCreadorActivo(true);
                diagramaRepository.save(diagrama);
            }

            Object diagramaJsonObj;
            try {
                diagramaJsonObj = objectMapper.readValue(diagrama.getDiagramaJson(), Map.class);
            } catch (JsonProcessingException e) {
                diagramaJsonObj = new HashMap<>();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", diagrama.getId());
            map.put("diagrama_json", diagramaJsonObj);
            return map;
        });
    }

    public List<Map<String, Object>> obtenerDiagramasPorUsuario(Long usuarioId) {
        List<Diagrama> diagramas = diagramaRepository.findByUsuarioId(usuarioId);
        return diagramas.stream()
                .map(d -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", d.getId());
                    map.put("nombre", d.getNombre());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Diagrama crearDiagrama(String nombre, Long usuarioId) {
        if (diagramaRepository.existsByNombreAndUsuarioId(nombre, usuarioId)) {
            throw new IllegalArgumentException("Ya existe un diagrama con ese nombre para el usuario.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Diagrama diagrama = new Diagrama();
        diagrama.setNombre(nombre);
        diagrama.setUsuario(usuario);
        diagrama.setUsuarioCreadorActivo(true);

        try {
            String jsonInicial = objectMapper.writeValueAsString(new HashMap<>());
            diagrama.setDiagramaJson(jsonInicial);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir el diagrama a JSON", e);
        }

        diagrama.setEnlaceInvitacion("temporal");

        Diagrama nuevoDiagrama = diagramaRepository.save(diagrama);

        String token = UUID.randomUUID().toString().split("-")[0];
        String enlace = "https://modelalive.app/diagrama/"
                + nuevoDiagrama.getId() + "/"
                + nombre.replaceAll("\\s+", "-")
                + "?token=" + token;

        nuevoDiagrama.setEnlaceInvitacion(enlace);
        diagramaRepository.desactivarOtrosDiagramas(usuarioId, nuevoDiagrama.getId());

        return diagramaRepository.save(nuevoDiagrama);
    }
}
