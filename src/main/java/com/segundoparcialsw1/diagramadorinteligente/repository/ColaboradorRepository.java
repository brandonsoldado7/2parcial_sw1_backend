package com.segundoparcialsw1.diagramadorinteligente.repository;

import com.segundoparcialsw1.diagramadorinteligente.model.Colaborador;
import com.segundoparcialsw1.diagramadorinteligente.model.ColaboradorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, ColaboradorId> {

    @Modifying
    @Query("UPDATE Colaborador c SET c.activo = false WHERE c.usuarioId = :usuarioId")
    void desactivarPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Colaborador c WHERE c.usuarioId = :usuarioId AND c.diagramaId = :diagramaId")
    Colaborador findByUsuarioIdAndDiagramaId(
            @Param("usuarioId") Long usuarioId,
            @Param("diagramaId") Long diagramaId
    );

    @Query("""
            SELECT u.id, u.color, u.sigla
            FROM Diagrama d
            JOIN d.usuario u
            WHERE d.id = :diagramaId
              AND d.usuarioCreadorActivo = true
            """)
    List<Object[]> findPropietarioActivo(@Param("diagramaId") Long diagramaId);

    @Query("""
            SELECT u.id, u.color, u.sigla
            FROM Colaborador c
            JOIN Usuario u ON c.usuarioId = u.id
            WHERE c.diagramaId = :diagramaId
              AND c.activo = true
            """)
    List<Object[]> findColaboradoresActivos(@Param("diagramaId") Long diagramaId);
}
