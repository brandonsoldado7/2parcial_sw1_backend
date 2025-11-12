package ${basePackage}.dto;

import lombok.Data;
import java.util.*;

/**
 * DTO generado automáticamente para la entidad ${dto.name}.
 * Sirve para transferir datos sin exponer directamente las entidades JPA.
 */
@Data
public class ${dto.name}DTO {

    private Long id; // ID autogenerado común a todas las entidades

    <#-- Atributos adicionales definidos en el modelo -->
    <#if dto.attributes?? && (dto.attributes?size > 0)>
        <#list dto.attributes as attr>
    private ${attr.type} ${attr.name};
        </#list>
    </#if>
}
