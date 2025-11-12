package com.segundoparcialsw1.diagramadorinteligente.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "diagramas")
@EntityListeners(AuditingEntityListener.class)
public class Diagrama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "enlace_invitacion", nullable = false)
    private String enlaceInvitacion;

    @Column(name = "usuario_creador_activo", nullable = false)
    private Boolean usuarioCreadorActivo = true;

    @Column(name = "diagrama_json", columnDefinition = "text")
    private String diagramaJson;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "colaboradores",
            joinColumns = @JoinColumn(name = "diagrama_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> colaboradores;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlaceInvitacion() {
        return enlaceInvitacion;
    }

    public void setEnlaceInvitacion(String enlaceInvitacion) {
        this.enlaceInvitacion = enlaceInvitacion;
    }

    public Boolean getUsuarioCreadorActivo() {
        return usuarioCreadorActivo;
    }

    public void setUsuarioCreadorActivo(Boolean usuarioCreadorActivo) {
        this.usuarioCreadorActivo = usuarioCreadorActivo;
    }

    public String getDiagramaJson() {
        return diagramaJson;
    }

    public void setDiagramaJson(String diagramaJson) {
        this.diagramaJson = diagramaJson;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(List<Usuario> colaboradores) {
        this.colaboradores = colaboradores;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
}



