package ${basePackage}.controller;

import ${basePackage}.model.${className};
import ${basePackage}.service.${className}Service;
<#if dto??>
import ${basePackage}.dto.${className}DTO;
import ${basePackage}.mapper.${className}Mapper;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST generado automáticamente para la entidad ${className}.
 * Gestiona operaciones CRUD básicas y admite DTOs si existen.
 */
@RestController
@RequestMapping("/api/${className?lower_case}")
@CrossOrigin(origins = "*")
public class ${className}Controller {

    @Autowired
    private ${className}Service service;

    <#if dto??>
    @Autowired
    private ${className}Mapper mapper;
    </#if>

    // CREATE
    @PostMapping("/guardar")
    public ResponseEntity<${className}<#if dto??>DTO</#if>> guardar(
            @RequestBody ${className}<#if dto??>DTO</#if> data) {

        <#if dto??>
        ${className} entity = mapper.toEntity(data);
        ${className} saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(saved));
        <#else>
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(data));
        </#if>
    }

    // READ ALL
    @GetMapping("/listar")
    public ResponseEntity<List<${className}<#if dto??>DTO</#if>>> listar() {
        List<${className}> list = service.findAll();

        <#if dto??>
        List<${className}DTO> dtoList = list.stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtoList);
        <#else>
        return ResponseEntity.ok(list);
        </#if>
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<${className}<#if dto??>DTO</#if>> obtenerPorId(@PathVariable Long id) {
        Optional<${className}> data = service.findById(id);

        if (data.isEmpty()) return ResponseEntity.notFound().build();

        <#if dto??>
        return ResponseEntity.ok(mapper.toDTO(data.get()));
        <#else>
        return ResponseEntity.ok(data.get());
        </#if>
    }

    // UPDATE
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<${className}<#if dto??>DTO</#if>> actualizar(
            @PathVariable Long id,
            @RequestBody ${className}<#if dto??>DTO</#if> data) {

        <#if dto??>
        ${className} entity = mapper.toEntity(data);
        <#else>
        ${className} entity = data;
        </#if>

        // Asignar ID por reflexión para mantener consistencia
        try {
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
        } catch (Exception ignored) {}

        ${className} updated = service.save(entity);

        <#if dto??>
        return ResponseEntity.ok(mapper.toDTO(updated));
        <#else>
        return ResponseEntity.ok(updated);
        </#if>
    }

    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    <#-- 🔹 Endpoints adicionales generados dinámicamente -->
    <#if endpointsHints??>
        <#list endpointsHints as ep>
            <#if ep.entity == className>
    @GetMapping("${ep.path}")
    public ResponseEntity<${ep.returns}> endpoint${ep_index}() {
        // TODO: Lógica personalizada IA
        return ResponseEntity.ok().build();
    }
            </#if>
        </#list>
    </#if>
}
