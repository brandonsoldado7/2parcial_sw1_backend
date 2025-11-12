import '../utils/json_utils.dart';
<#if (entity.inheritance)!?has_content>
<#assign parentSnake = camelToSnake(entity.inheritance)>
import '${parentSnake}.dart';
</#if>

<#if (entity.relations)!?has_content>
  <#list entity.relations as rel>
    <#if (rel.type == "ManyToOne") || (rel.type == "OneToOne")>
    <#assign relSnake = camelToSnake(rel.target)>
import '${relSnake}.dart';
    </#if>
  </#list>
</#if>

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class ${className}<#if (entity.inheritance)!?has_content> extends ${(entity.inheritance)!}</#if> {
  // 🔹 Atributos propios
  <#list (entity.attributes)![] as attr>
    <#-- No incluir id si es herencia -->
    <#if !(((entity.inheritance)!?has_content) && (attr.name == "id"))>
  ${javaToDartType(attr.type)}? ${attr.name};
    </#if>
  </#list>

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  <#if (entity.relations)!?has_content>
    <#list entity.relations as rel>
      <#if (rel.type == "ManyToOne") || (rel.type == "OneToOne")>
  ${(rel.target)!}? ${(rel.target?lower_case)!};
      </#if>
    </#list>
  </#if>

  // 🔹 Constructor
  ${className}({
    <#if (parentAttributes)!?has_content>
      <#list parentAttributes as pAttr>
    ${javaToDartType(pAttr.type)}? ${pAttr.name},
      </#list>
    </#if>
    <#list (entity.attributes)![] as attr>
      <#if !(((entity.inheritance)!?has_content) && (attr.name == "id"))>
      <#assign needComma = (attr_has_next) || ((entity.relations)!?has_content)>
      this.${attr.name}<#if needComma>,</#if>
      </#if>
    </#list>
    <#if (entity.relations)!?has_content>
      <#list entity.relations as rel>
        <#if (rel.type == "ManyToOne") || (rel.type == "OneToOne")>
    this.${(rel.target?lower_case)!}<#if rel_has_next>,</#if>
        </#if>
      </#list>
    </#if>
  })
  <#if (((parentAttributes)!?has_content) && (parentAttributes?size > 0))> : super(
    <#list parentAttributes as pAttr>
    ${pAttr.name}: ${pAttr.name}<#if pAttr_has_next>,</#if>
    </#list>
  )</#if>;

    // 🔹 fromJson factory
    factory ${className}.fromJson(Map<String, dynamic> json) {
    return ${className}(
    <#if (parentAttributes)!?has_content>
      <#list parentAttributes as pAttr>
    ${pAttr.name}: autoConvert<${javaToDartType(pAttr.type)}>(json['${pAttr.name}']),
      </#list>
    </#if>
    <#list (entity.attributes)![] as attr>
      <#if !(((entity.inheritance)!?has_content) && (attr.name == "id"))>
    ${attr.name}: autoConvert<${javaToDartType(attr.type)}>(json['${attr.name}'])<#if (attr_has_next) || ((entity.relations)!?has_content)>,</#if>
      </#if>
    </#list>
    <#if (entity.relations)!?has_content>
      <#list entity.relations as rel>
        <#if (rel.type == "ManyToOne") || (rel.type == "OneToOne")>
    ${(rel.target?lower_case)!}:
      (json['${(rel.target?lower_case)!}'] is Map<String, dynamic>)
          ? ${(rel.target)!}.fromJson(json['${(rel.target?lower_case)!}'])
          : (json['${(rel.target?lower_case)!}'] != null
              ? ${(rel.target)!}(id: autoConvert<int>(json['${(rel.target?lower_case)!}']))
              : null)<#if rel_has_next>,</#if>
        </#if>
      </#list>
    </#if>
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {
      <#if (entity.inheritance)!?has_content>
      ...super.toJson(),
      </#if>

      <#-- 🔹 Atributos normales -->
      <#list (entity.attributes)![] as attr>
        <#assign needComma1 = (attr_has_next) || ((entity.relations)!?has_content)>
        <#if javaToDartType(attr.type) == "DateTime">
      '${attr.name}': ${attr.name}?.toIso8601String().split('T').first<#if needComma1>,</#if>
        <#else>
      '${attr.name}': ${attr.name}<#if needComma1>,</#if>
        </#if>
      </#list>

      <#-- 🔹 Relaciones (ManyToOne / OneToOne) -->
      <#if (entity.relations)!?has_content>
        <#list entity.relations as rel>
          <#if (rel.type == "ManyToOne") || (rel.type == "OneToOne")>
      '${rel.target?lower_case}': ${rel.target?lower_case} != null ? {'id': ${rel.target?lower_case}!.id} : null,
          </#if>
        </#list>
      </#if>
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
      <#-- Buscar los dos primeros atributos definidos -->
      <#assign attrs = (entity.attributes)![]>
      <#if (attrs?? && (attrs?size > 0))>
        return '${"$"}{${attrs[0].name} ?? "s/d"}';
      <#else>
        return '${entity.name} sin datos';
      </#if>
    }


}
