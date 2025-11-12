package com.segundoparcialsw1.diagramadorinteligente.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.segundoparcialsw1.diagramadorinteligente.model.Colaborador;
import com.segundoparcialsw1.diagramadorinteligente.model.Diagrama;
import com.segundoparcialsw1.diagramadorinteligente.repository.ColaboradorRepository;
import com.segundoparcialsw1.diagramadorinteligente.repository.DiagramaRepository;

import java.util.*;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final DiagramaRepository diagramaRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository,
                              DiagramaRepository diagramaRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.diagramaRepository = diagramaRepository;
    }

    /** Registrar colaborador mediante enlace de invitación y usuarioId. */
    @Transactional
    public Optional<Diagrama> registrarColaborador(String enlaceInvitacion, Long usuarioId) {
        Diagrama diagrama = diagramaRepository.findAll().stream()
                .filter(d -> d.getEnlaceInvitacion().equals(enlaceInvitacion))
                .findFirst()
                .orElse(null);

        if (diagrama == null) {
            return Optional.empty();
        }

        diagramaRepository.findByUsuarioId(usuarioId)
                .forEach(d -> {
                    d.setUsuarioCreadorActivo(false);
                    diagramaRepository.save(d);
                });

        Colaborador colaborador = colaboradorRepository.findByUsuarioIdAndDiagramaId(usuarioId, diagrama.getId());

        if (colaborador == null) {
            colaborador = new Colaborador();
            colaborador.setUsuarioId(usuarioId);
            colaborador.setDiagramaId(diagrama.getId());
            colaborador.setActivo(true);
        } else if (!colaborador.getActivo()) {
            colaborador.setActivo(true);
        }

        colaboradorRepository.save(colaborador);
        return Optional.of(diagrama);
    }

    public List<Map<String, Object>> obtenerColorYSiglaUsuarios(Long diagramaId) {
        List<Map<String, Object>> lista = new ArrayList<>();
        agregarPropietarioActivo(lista, diagramaId);
        agregarColaboradoresActivos(lista, diagramaId);
        return lista;
    }

    private void agregarPropietarioActivo(List<Map<String, Object>> lista, Long diagramaId) {
        for (Object[] row : colaboradorRepository.findPropietarioActivo(diagramaId)) {
            lista.add(Map.of(
                    "usuarioId", row[0],
                    "color", row[1],
                    "sigla", row[2]
            ));
        }
    }

    private void agregarColaboradoresActivos(List<Map<String, Object>> lista, Long diagramaId) {
        for (Object[] row : colaboradorRepository.findColaboradoresActivos(diagramaId)) {
            lista.add(Map.of(
                    "usuarioId", row[0],
                    "color", row[1],
                    "sigla", row[2]
            ));
        }
    }

    /** Desactiva todas las colaboraciones y diagramas del usuario. */
    @Transactional
    public void desactivarColaboracionesYDiagramas(Long usuarioId) {
        diagramaRepository.findByUsuarioId(usuarioId)
                .forEach(d -> {
                    d.setUsuarioCreadorActivo(false);
                    diagramaRepository.save(d);
                });

        colaboradorRepository.desactivarPorUsuario(usuarioId);
    }

    /** Obtener enlace de invitación por id de diagrama. */
    public Optional<String> obtenerEnlaceInvitacion(Long id) {
        return diagramaRepository.findById(id)
                .map(Diagrama::getEnlaceInvitacion);
    }
}
