package ${basePackage}.service;

import ${basePackage}.model.${className};
import ${basePackage}.repository.${className}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio generado automáticamente para la entidad ${className}.
 * Proporciona operaciones CRUD básicas y puede ampliarse con lógica personalizada.
 */
@Service
public class ${className}Service {

    @Autowired
    private ${className}Repository repository;

    // CREATE o UPDATE
    public ${className} save(${className} entity) {
        return repository.save(entity);
    }

    // READ ALL
    public List<${className}> findAll() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<${className}> findById(Long id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            System.err.println("⚠️ Error buscando ${className} por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // DELETE
    public boolean delete(Long id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("⚠️ Error eliminando ${className} con ID " + id + ": " + e.getMessage());
            return false;
        }
    }
}
