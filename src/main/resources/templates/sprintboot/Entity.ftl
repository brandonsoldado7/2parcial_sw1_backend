package ${basePackage}.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;
import java.time.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)

<#-- ============================================================
     💡 Herencia
     - Si la entidad tiene subclases → @Inheritance en la clase base
     - Si hereda de otra → extends Padre
 ============================================================ -->
<#if entity.hasSubclasses?? && entity.hasSubclasses>
@Inheritance(strategy = InheritanceType.JOINED)
</#if>
public class ${entity.name}<#if entity.extends?? && entity.extends != "null"> extends ${entity.extends}</#if> {


    <#-- ===========================================================
         🔹 ID Long autogenerado
         - Solo en clases base (no subclases)
     =========================================================== -->
    <#if !(entity.isSubclass?? && entity.isSubclass)>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    </#if>

    <#-- ===========================================================
         🔹 Atributos normales (omitimos los 'id')
     =========================================================== -->
    <#if entity.attributes?? && (entity.attributes?size > 0)>
        <#list entity.attributes as att>
            <#if !att.name?lower_case?starts_with("id")>
    private ${att.type} ${att.name};
            </#if>
        </#list>
    </#if>

    <#-- ===========================================================
         🔹 Relaciones UML → JPA (totalmente revisadas) y jsonignore
         - Association (1:1, 1:N, N:1, N:N con tabla intermedia)
         - Composition / Aggregation
         - Dependency
         - Herencia ya gestionada arriba
     =========================================================== -->
    <#if entity.relations?? && (entity.relations?size > 0)>
        <#list entity.relations as rel>

            <#assign isOwner = (rel.owningSide?? && rel.owningSide) />
            <#assign applyIgnore = (rel.jsonIgnore?? && rel.jsonIgnore) />

            <#-- =======================================
                 🔹 1. Tablas intermedias (*:*)
            ======================================= -->
            <#if (rel.hasThroughEntity?? && rel.hasThroughEntity)>
                <#if rel.role?? && rel.role == "extremo">
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToMany(mappedBy = "${entity.name?uncap_first}")
    private List<${rel.throughEntity}> ${rel.throughEntity?uncap_first}s;
                <#elseif rel.role?? && rel.role == "intermedia">
    <#if applyIgnore>@JsonIgnore</#if>
    @ManyToOne
    @JoinColumn(name = "${rel.target?uncap_first}_id", referencedColumnName = "id")
    private ${rel.target} ${rel.target?uncap_first};
                </#if>
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 2. Composición (Padre ↔ Hijo)
            ======================================= -->
            <#if rel.composition?? && rel.composition>
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToMany(mappedBy = "${entity.name?uncap_first}", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<${rel.target}> ${rel.target?uncap_first}s;
                <#continue>
            </#if>

            <#if rel.composition_inversed?? && rel.composition_inversed>
    <#if applyIgnore>@JsonIgnore</#if>
    @ManyToOne
    @JoinColumn(name = "${rel.target?uncap_first}_id", referencedColumnName = "id")
    private ${rel.target} ${rel.target?uncap_first};
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 3. Agregación (Padre ↔ Hijo)
            ======================================= -->
            <#if rel.aggregation?? && rel.aggregation>
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToMany(mappedBy = "${entity.name?uncap_first}", cascade = CascadeType.PERSIST)
    private List<${rel.target}> ${rel.target?uncap_first}s;
                <#continue>
            </#if>

            <#if rel.aggregation_inversed?? && rel.aggregation_inversed>
    <#if applyIgnore>@JsonIgnore</#if>
    @ManyToOne
    @JoinColumn(name = "${rel.target?uncap_first}_id", referencedColumnName = "id")
    private ${rel.target} ${rel.target?uncap_first};
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 4. Dependencia (no persistente)
            ======================================= -->
            <#if rel.jpaType == "Transient">
    <#if applyIgnore>@JsonIgnore</#if>
    @Transient
    ${rel.comment!"// Dependencia lógica no persistente"}
    private ${rel.target} ${rel.target?uncap_first};
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 5. Asociación 1:1
            ======================================= -->
            <#if rel.jpaType == "OneToOne">
                <#if isOwner>
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToOne
    @JoinColumn(name = "${rel.target?uncap_first}_id", referencedColumnName = "id")
                <#else>
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToOne(mappedBy = "${entity.name?uncap_first}")
                </#if>
    private ${rel.target} ${rel.target?uncap_first};
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 6. Asociación 1:N o N:1
            ======================================= -->
            <#if rel.jpaType == "OneToMany" || rel.jpaType == "ManyToOne">
                <#if rel.jpaType == "OneToMany">
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToMany(mappedBy = "${entity.name?uncap_first}")
    private List<${rel.target}> ${rel.target?uncap_first}s;
                <#else>
    <#if applyIgnore>@JsonIgnore</#if>
    @ManyToOne
    @JoinColumn(name = "${rel.target?uncap_first}_id", referencedColumnName = "id")
    private ${rel.target} ${rel.target?uncap_first};
                </#if>
                <#continue>
            </#if>

            <#-- =======================================
                 🔹 7. Asociación N:N (sin intermedia explícita)
                 🚫 Nunca @ManyToMany → tabla intermedia aparte
            ======================================= -->
            <#if rel.jpaType == "ManyToMany">
    <#if applyIgnore>@JsonIgnore</#if>
    @OneToMany(mappedBy = "${entity.name?uncap_first}")
    private List<${rel.target}> ${rel.target?uncap_first}s;
                <#continue>
            </#if>

        </#list>
    </#if>

}
