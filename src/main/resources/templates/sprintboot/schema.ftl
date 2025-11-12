<#list classes as c>
CREATE TABLE ${c.name?lower_case} (
    id BIGSERIAL PRIMARY KEY
    <#list c.attributes as attr>,
    ${attr.name}
        <#if attr.type?lower_case == "string">
            VARCHAR(255)
        <#elseif attr.type?lower_case == "int" || attr.type?lower_case == "integer">
            INTEGER
        <#elseif attr.type?lower_case == "long">
            BIGINT
        <#elseif attr.type?lower_case == "double" || attr.type?lower_case == "float">
            NUMERIC
        <#elseif attr.type?lower_case == "boolean">
            BOOLEAN
        <#else>
            VARCHAR(255)
        </#if>
    </#list>
);
</#list>
