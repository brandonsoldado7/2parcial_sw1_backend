# 🧠 Backend generado automáticamente 🚀

**Paquete base:** `${basePackage}`
**Fecha de generación:** ${.now?string["yyyy-MM-dd HH:mm:ss"]}

---

## 📦 Entidades generadas:
<#if entities?? && (entities?size > 0)>
<#list entities as e>
- **${e.name}**
    <#if e.extends?? && e.extends?has_content>(hereda de ${e.extends})</#if>
</#list>
<#else>
*(Sin entidades definidas en el modelo)*
</#if>

---

## 🔁 Endpoints CRUD comunes:

| Método | Ruta | Descripción |
|--------|------|--------------|
| **GET** | `/api/{entidad}/listar` | Listar todos los registros |
| **GET** | `/api/{entidad}/{id}` | Obtener un registro por ID |
| **POST** | `/api/{entidad}/guardar` | Crear un nuevo registro |
| **PUT** | `/api/{entidad}/actualizar/{id}` | Actualizar un registro existente |
| **DELETE** | `/api/{entidad}/eliminar/{id}` | Eliminar un registro |

---

> ⚙️ Generado automáticamente mediante **CodeGenService (IA + FreeMarker)**
> Proyecto autogenerado basado en modelo JSON enriquecido 🧩
