package com.segundoparcialsw1.diagramadorinteligente.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidoP;

    @Column(nullable = false)
    private String apellidoM;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "codigo_pais", nullable = false)
    private Integer codigoPais;

    @Column(nullable = false)
    private Integer telefono;

    @Column
    private String color;

    @Column(nullable = true, length = 1)
    private String sigla;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Diagrama> diagramas;

    @ManyToMany(mappedBy = "colaboradores")
    private List<Diagrama> diagramasColaborados;

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

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
        if (apellidoP != null && !apellidoP.isEmpty()) {
            String trimmed = apellidoP.trim();
            this.sigla = trimmed.isEmpty() ? null : String.valueOf(trimmed.charAt(0)).toUpperCase();
        } else {
            this.sigla = null;
        }
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(Integer codigoPais) {
        this.codigoPais = codigoPais;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSigla() {
        return sigla;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public List<Diagrama> getDiagramas() {
        return diagramas;
    }

    public void setDiagramas(List<Diagrama> diagramas) {
        this.diagramas = diagramas;
    }

    public List<Diagrama> getDiagramasColaborados() {
        return diagramasColaborados;
    }

    public void setDiagramasColaborados(List<Diagrama> diagramasColaborados) {
        this.diagramasColaborados = diagramasColaborados;
    }
}
