package com.segundoparcialsw1.diagramadorinteligente.model;

import java.io.Serializable;
import java.util.Objects;

public class ColaboradorId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long usuarioId;
    private Long diagramaId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getDiagramaId() {
        return diagramaId;
    }

    public void setDiagramaId(Long diagramaId) {
        this.diagramaId = diagramaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColaboradorId)) return false;
        ColaboradorId that = (ColaboradorId) o;
        return Objects.equals(usuarioId, that.usuarioId) &&
               Objects.equals(diagramaId, that.diagramaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, diagramaId);
    }
}
