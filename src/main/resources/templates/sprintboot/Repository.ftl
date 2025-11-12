package ${basePackage}.repository;

import ${basePackage}.model.${className};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad ${className}.
 * Gestiona operaciones CRUD básicas con un ID autogenerado (Long).
 */
@Repository
public interface ${className}Repository extends JpaRepository<${className}, Long> {
}
