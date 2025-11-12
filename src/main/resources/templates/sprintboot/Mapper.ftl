package ${basePackage}.mapper;

import ${basePackage}.model.${className};
import ${basePackage}.dto.${className}DTO;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper generado automáticamente con MapStruct.
 * Convierte entre la entidad ${className} y su DTO correspondiente.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Mapper {

    // Conversión individual
    ${className}DTO toDTO(${className} entity);
    ${className} toEntity(${className}DTO dto);

    // Conversión de listas
    List<${className}DTO> toDTOList(List<${className}> entities);
    List<${className}> toEntityList(List<${className}DTO> dtos);
}
