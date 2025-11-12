package com.segundoparcialsw1.diagramadorinteligente.repository;

import com.segundoparcialsw1.diagramadorinteligente.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);

    Usuario findByCorreo(String correo);
}
