package com.segundoparcialsw1.diagramadorinteligente.repository;

import com.segundoparcialsw1.diagramadorinteligente.model.Diagrama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagramaRepository extends JpaRepository<Diagrama, Long> {

    @Modifying
    @Query("UPDATE Diagrama d SET d.usuarioCreadorActivo = false " +
           "WHERE d.usuario.id = :usuarioId AND d.id <> :diagramaId")
    void desactivarOtrosDiagramas(
            @Param("usuarioId") Long usuarioId,
            @Param("diagramaId") Long diagramaId
    );

    boolean existsByNombreAndUsuarioId(String nombre, Long usuarioId);

    List<Diagrama> findByUsuarioId(Long usuarioId);
}
