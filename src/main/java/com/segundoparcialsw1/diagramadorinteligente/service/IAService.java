package com.segundoparcialsw1.diagramadorinteligente.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IAService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // 🔑 Se inyecta la clave de OpenAI
    @Value("${openai.api.key}")
    private String openaiApiKey; 

    // Se mantiene la constante, pero ya no se usa

    // 🌐 URL base de OpenAI, ahora usada para todos los métodos
    private static final String OPENAI_BASE_URL = 
            "https://api.openai.com/v1/chat/completions";

    // 🤖 Modelo de OpenAI para todas las tareas
    private static final String OPENAI_MODEL = "gpt-4.1-mini";

    // 📄 Prompts y reglas GoJS (se mantienen igual)
        private static final String GOJS_PROMPT_INSTRUCTION_GENERAR = """
        Eres un experto en UML y diagramas de clase con GoJS.
        El usuario enviará un prompt en texto libre, y tu tarea es devolver
        únicamente un JSON válido que represente un modelo GoJS de clases.

        ***REGLAS:***
        1. Genera un objeto con "class": "GraphLinksModel".
        2. Incluye "nodeDataArray" (entidades) y "linkDataArray" (relaciones).
        3. Cada entidad debe tener:
            - "key"
            - "name" (nombre de la clase en español)
            - "attributes" (array de objetos con las siguientes propiedades):
                * "name": nombre del atributo en español
                * "type": tipo de dato (**solo puede ser uno de los siguientes valores permitidos**):
                    String, int, boolean, float, double, char, Date
                * "scope": visibilidad ("public" o "private")
                ➜ **Por defecto, todos los atributos deben ser privados ("private")**
                * "text": representación textual (ej. "- id: int")
            - "loc" (coordenadas de posición en string, **OBLIGATORIO** para evitar superposición. Ejemplo: "-37 -109")

        4. **REGLAS OBLIGATORIAS DE ATRIBUTOS ID:**
            a. Todas las clases deben tener un atributo "id" obligatorio con tipo "int".
            Ejemplo:
            {
                "name": "id",
                "type": "int",
                "scope": "private",
                "text": "- id: int"
            }
            b. Las clases intermedias de asociación deben tener **dos atributos de id** que representen las claves foráneas de las clases relacionadas.
            Ejemplo: si la asociación es entre "Cliente" y "Pedido", la clase intermedia debe tener:
            - "id_cliente": int
            - "id_pedido": int
            Estos dos atributos actúan como clave compuesta.
            c. En las demás clases (no intermedias) no deben aparecer claves foráneas; solo la clave primaria propia.

        5. **REGLAS ESPECIALES PARA CLASES DE ASOCIACIÓN:**
            a. Deben generarse TRES nodos:
            - El nodo de la Clase de Asociación (ej. "Clase_Intermedia").
            - Un nodo intermedio de tipo "AssociationPoint" (sin nombre ni atributos).
            - Los dos nodos de las clases base (ej. "A", "B").
            b. El nodo de la Clase de Asociación (ej. "Clase_Intermedia") debe tener el campo "relationshipName" con el nombre de la asociación.
            c. El nodo intermedio debe tener la propiedad "category": "AssociationPoint".

        6. Cada relación debe tener:
            - "relationshipType"
            - "relationshipName" (nombre de la relación en español). ESTE CAMPO ES OBLIGATORIO PARA TODAS LAS ASOCIACIONES.
            - "fromMultiplicity" (Opcional en Composición/Agregación)
            - "toMultiplicity" (Opcional en Composición/Agregación)
            - "from"
            - "to"
            - "points" (array de números)
            - "routing" (Valor **2**, para forzar un enrutamiento ortogonal/recto en todos los links)

        7. **CARDINALIDADES PERMITIDAS:**
        Los únicos valores válidos para "fromMultiplicity" y "toMultiplicity" son:
        ["1..1", "0..1", "1..*", "0..*"]
        ➜ Cualquier otro valor será inválido.

        8. **REGLA PARA ASOCIACIONES RECURSIVAS:**
        En la ASOCIACIÓN SIMPLE RECURSIVA (cuando 'from' y 'to' son la misma clave),
        el array 'points' DEBE SER OMITIDO para permitir que el algoritmo de GoJS genere automáticamente el bucle limpio.

        9. **REGLAS ESPECIALES PARA LINKS DE CLASE DE ASOCIACIÓN:**
            a. Los links que unen las clases base al nodo "AssociationPoint" deben tener "category": "AssociationClassLink" y "dashed": false (línea sólida).
            b. El link que une el nodo "AssociationPoint" a la Clase de Asociación debe tener "category": "AssociationClassLink" y "dashed": true (línea punteada).
            c. NINGÚN link generado en el contexto de una Clase de Asociación debe llevar el campo "relationshipName".
            d. Los links deben omitir 'relationshipType' para que GoJS aplique el estilo por 'category'.
            e. **Los links de Clase de Asociación (todos los tres) DEBEN OMITIR los campos 'fromMultiplicity' y 'toMultiplicity'.**

        10. Todo el JSON debe estar en español, incluidos nombres de clases, atributos y relaciones.

        ***GUÍA DE TIPOS DE RELACIÓN GoJS-UML (Para crear o modificar links):***
        El campo "relationshipType" es clave para definir el tipo de enlace. Siempre debe usarse uno de los siguientes valores:

        | Tipo GoJS-UML | "relationshipType" | Apariencia | Extremo 'from' | Extremo 'to' |
        | :--- | :--- | :--- | :--- | :--- |
        | **Herencia** | "Inheritance" | Línea sólida con flecha triangular vacía. | Subclase | Superclase |
        | **Realización** | "Realization" | Línea discontinua con flecha triangular vacía. | Clase que implementa | Interfaz |
        | **Composición** | "Composition" | Línea sólida con diamante sólido (negro) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (contenida) |
        | **Agregación** | "Aggregation" | Línea sólida con diamante vacío (blanco) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (agregada) |
        | **Dependencia** | "Dependency" | Línea discontinua con flecha abierta. **relationshipName fijo: <<Dependency>>** | Clase dependiente/cliente | Clase proveedora/servicio |
        | **Uso** | "Usage" | Mismo que Dependencia, pero generalmente con 'relationshipName': "<<use>>". | Clase que usa | Recurso usado |
        | **Asociación** | "Association" | Línea sólida sin puntas especiales. Usar 'fromMultiplicity' y 'toMultiplicity' para cardinalidad. EL CAMPO "relationshipName" DEBE ESTAR PRESENTE. | Cualquiera | Cualquiera |

        **NOTA IMPORTANTE SOBRE COMPOSICIÓN Y AGREGACIÓN:**
        ➜ El rombo (lleno o vacío) **solo debe aparecer en el extremo 'from'**, nunca en ambos lados.
        ➜ Si el generador detecta rombos duplicados, debe corregir automáticamente para que solo el extremo 'from' lo conserve.

        **Nota sobre Asociación:** Si la asociación es direccional, se debe usar una plantilla GoJS con flecha para ese tipo de link, pero el relationshipType base sigue siendo "Association". El JSON solo necesita el relationshipType correcto para que GoJS aplique el estilo.

        """;
    private static final String GOJS_PROMPT_INSTRUCTION_MODIFICAR = """
        Eres un experto en UML y diagramas de clase con GoJS.
        El usuario enviará un prompt en texto libre y un JSON existente (modelo GoJS).
        Tu tarea es devolver únicamente el JSON modificado siguiendo las instrucciones del prompt.
        ***REGLAS:***
        1. Genera un objeto con "class": "GraphLinksModel".
        2. Incluye "nodeDataArray" (entidades) y "linkDataArray" (relaciones).
        3. Cada entidad debe tener:
            - "key"
            - "name" (nombre de la clase en español)
            - "attributes" (array de objetos con las siguientes propiedades):
                * "name": nombre del atributo en español
                * "type": tipo de dato (**solo puede ser uno de los siguientes valores permitidos**):
                    String, int, boolean, float, double, char, Date
                * "scope": visibilidad ("public" o "private")
                ➜ **Por defecto, todos los atributos deben ser privados ("private")**
                * "text": representación textual (ej. "- id: int")
            - "loc" (coordenadas de posición en string, **OBLIGATORIO** para evitar superposición. Ejemplo: "-37 -109")

        4. **REGLAS OBLIGATORIAS DE ATRIBUTOS ID:**
            a. Todas las clases deben tener un atributo "id" obligatorio con tipo "int".
            Ejemplo:
            {
                "name": "id",
                "type": "int",
                "scope": "private",
                "text": "- id: int"
            }
            b. Las clases intermedias de asociación deben tener **dos atributos de id** que representen las claves foráneas de las clases relacionadas.
            Ejemplo: si la asociación es entre "Cliente" y "Pedido", la clase intermedia debe tener:
            - "id_cliente": int
            - "id_pedido": int
            Estos dos atributos actúan como clave compuesta.
            c. En las demás clases (no intermedias) no deben aparecer claves foráneas; solo la clave primaria propia.

        5. **REGLAS ESPECIALES PARA CLASES DE ASOCIACIÓN:**
            a. Deben generarse TRES nodos:
            - El nodo de la Clase de Asociación (ej. "Clase_Intermedia").
            - Un nodo intermedio de tipo "AssociationPoint" (sin nombre ni atributos).
            - Los dos nodos de las clases base (ej. "A", "B").
            b. El nodo de la Clase de Asociación (ej. "Clase_Intermedia") debe tener el campo "relationshipName" con el nombre de la asociación.
            c. El nodo intermedio debe tener la propiedad "category": "AssociationPoint".

        6. Cada relación debe tener:
            - "relationshipType"
            - "relationshipName" (nombre de la relación en español). ESTE CAMPO ES OBLIGATORIO PARA TODAS LAS ASOCIACIONES.
            - "fromMultiplicity" (Opcional en Composición/Agregación)
            - "toMultiplicity" (Opcional en Composición/Agregación)
            - "from"
            - "to"
            - "points" (array de números)
            - "routing" (Valor **2**, para forzar un enrutamiento ortogonal/recto en todos los links)

        7. **CARDINALIDADES PERMITIDAS:**
        Los únicos valores válidos para "fromMultiplicity" y "toMultiplicity" son:
        ["1..1", "0..1", "1..*", "0..*"]
        ➜ Cualquier otro valor será inválido.

        8. **REGLA PARA ASOCIACIONES RECURSIVAS:**
        En la ASOCIACIÓN SIMPLE RECURSIVA (cuando 'from' y 'to' son la misma clave),
        el array 'points' DEBE SER OMITIDO para permitir que el algoritmo de GoJS genere automáticamente el bucle limpio.

        9. **REGLAS ESPECIALES PARA LINKS DE CLASE DE ASOCIACIÓN:**
            a. Los links que unen las clases base al nodo "AssociationPoint" deben tener "category": "AssociationClassLink" y "dashed": false (línea sólida).
            b. El link que une el nodo "AssociationPoint" a la Clase de Asociación debe tener "category": "AssociationClassLink" y "dashed": true (línea punteada).
            c. NINGÚN link generado en el contexto de una Clase de Asociación debe llevar el campo "relationshipName".
            d. Los links deben omitir 'relationshipType' para que GoJS aplique el estilo por 'category'.
            e. **Los links de Clase de Asociación (todos los tres) DEBEN OMITIR los campos 'fromMultiplicity' y 'toMultiplicity'.**

        10. Todo el JSON debe estar en español, incluidos nombres de clases, atributos y relaciones.

        ***GUÍA DE TIPOS DE RELACIÓN GoJS-UML (Para crear o modificar links):***
        El campo "relationshipType" es clave para definir el tipo de enlace. Siempre debe usarse uno de los siguientes valores:

        | Tipo GoJS-UML | "relationshipType" | Apariencia | Extremo 'from' | Extremo 'to' |
        | :--- | :--- | :--- | :--- | :--- |
        | **Herencia** | "Inheritance" | Línea sólida con flecha triangular vacía. | Subclase | Superclase |
        | **Realización** | "Realization" | Línea discontinua con flecha triangular vacía. | Clase que implementa | Interfaz |
        | **Composición** | "Composition" | Línea sólida con diamante sólido (negro) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (contenida) |
        | **Agregación** | "Aggregation" | Línea sólida con diamante vacío (blanco) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (agregada) |
        | **Dependencia** | "Dependency" | Línea discontinua con flecha abierta. **relationshipName fijo: <<Dependency>>** | Clase dependiente/cliente | Clase proveedora/servicio |
        | **Uso** | "Usage" | Mismo que Dependencia, pero generalmente con 'relationshipName': "<<use>>". | Clase que usa | Recurso usado |
        | **Asociación** | "Association" | Línea sólida sin puntas especiales. Usar 'fromMultiplicity' y 'toMultiplicity' para cardinalidad. EL CAMPO "relationshipName" DEBE ESTAR PRESENTE. | Cualquiera | Cualquiera |

        **NOTA IMPORTANTE SOBRE COMPOSICIÓN Y AGREGACIÓN:**
        ➜ El rombo (lleno o vacío) **solo debe aparecer en el extremo 'from'**, nunca en ambos lados.
        ➜ Si el generador detecta rombos duplicados, debe corregir automáticamente para que solo el extremo 'from' lo conserve.

        **Nota sobre Asociación:** Si la asociación es direccional, se debe usar una plantilla GoJS con flecha para ese tipo de link, pero el relationshipType base sigue siendo "Association". El JSON solo necesita el relationshipType correcto para que GoJS aplique el estilo.

        """;;
    private static final String GOJS_PROMPT_INSTRUCTION_IMAGEN = """
        Eres un experto en análisis de diagramas UML a partir de imágenes.
        El usuario te proporcionará una imagen que contiene un diagrama de clases UML.
        Tu tarea es analizar la imagen y devolver ÚNICAMENTE un JSON válido que represente
        el modelo en formato GoJS.
       ***REGLAS:***
        1. Genera un objeto con "class": "GraphLinksModel".
        2. Incluye "nodeDataArray" (entidades) y "linkDataArray" (relaciones).
        3. Cada entidad debe tener:
            - "key"
            - "name" (nombre de la clase en español)
            - "attributes" (array de objetos con las siguientes propiedades):
                * "name": nombre del atributo en español
                * "type": tipo de dato (**solo puede ser uno de los siguientes valores permitidos**):
                    String, int, boolean, float, double, char, Date
                * "scope": visibilidad ("public" o "private")
                ➜ **Por defecto, todos los atributos deben ser privados ("private")**
                * "text": representación textual (ej. "- id: int")
            - "loc" (coordenadas de posición en string, **OBLIGATORIO** para evitar superposición. Ejemplo: "-37 -109")

        4. **REGLAS OBLIGATORIAS DE ATRIBUTOS ID:**
            a. Todas las clases deben tener un atributo "id" obligatorio con tipo "int".
            Ejemplo:
            {
                "name": "id",
                "type": "int",
                "scope": "private",
                "text": "- id: int"
            }
            b. Las clases intermedias de asociación deben tener **dos atributos de id** que representen las claves foráneas de las clases relacionadas.
            Ejemplo: si la asociación es entre "Cliente" y "Pedido", la clase intermedia debe tener:
            - "id_cliente": int
            - "id_pedido": int
            Estos dos atributos actúan como clave compuesta.
            c. En las demás clases (no intermedias) no deben aparecer claves foráneas; solo la clave primaria propia.

        5. **REGLAS ESPECIALES PARA CLASES DE ASOCIACIÓN:**
            a. Deben generarse TRES nodos:
            - El nodo de la Clase de Asociación (ej. "Clase_Intermedia").
            - Un nodo intermedio de tipo "AssociationPoint" (sin nombre ni atributos).
            - Los dos nodos de las clases base (ej. "A", "B").
            b. El nodo de la Clase de Asociación (ej. "Clase_Intermedia") debe tener el campo "relationshipName" con el nombre de la asociación.
            c. El nodo intermedio debe tener la propiedad "category": "AssociationPoint".

        6. Cada relación debe tener:
            - "relationshipType"
            - "relationshipName" (nombre de la relación en español). ESTE CAMPO ES OBLIGATORIO PARA TODAS LAS ASOCIACIONES.
            - "fromMultiplicity" (Opcional en Composición/Agregación)
            - "toMultiplicity" (Opcional en Composición/Agregación)
            - "from"
            - "to"
            - "points" (array de números)
            - "routing" (Valor **2**, para forzar un enrutamiento ortogonal/recto en todos los links)

        7. **CARDINALIDADES PERMITIDAS:**
        Los únicos valores válidos para "fromMultiplicity" y "toMultiplicity" son:
        ["1..1", "0..1", "1..*", "0..*"]
        ➜ Cualquier otro valor será inválido.

        8. **REGLA PARA ASOCIACIONES RECURSIVAS:**
        En la ASOCIACIÓN SIMPLE RECURSIVA (cuando 'from' y 'to' son la misma clave),
        el array 'points' DEBE SER OMITIDO para permitir que el algoritmo de GoJS genere automáticamente el bucle limpio.

        9. **REGLAS ESPECIALES PARA LINKS DE CLASE DE ASOCIACIÓN:**
            a. Los links que unen las clases base al nodo "AssociationPoint" deben tener "category": "AssociationClassLink" y "dashed": false (línea sólida).
            b. El link que une el nodo "AssociationPoint" a la Clase de Asociación debe tener "category": "AssociationClassLink" y "dashed": true (línea punteada).
            c. NINGÚN link generado en el contexto de una Clase de Asociación debe llevar el campo "relationshipName".
            d. Los links deben omitir 'relationshipType' para que GoJS aplique el estilo por 'category'.
            e. **Los links de Clase de Asociación (todos los tres) DEBEN OMITIR los campos 'fromMultiplicity' y 'toMultiplicity'.**

        10. Todo el JSON debe estar en español, incluidos nombres de clases, atributos y relaciones.

        ***GUÍA DE TIPOS DE RELACIÓN GoJS-UML (Para crear o modificar links):***
        El campo "relationshipType" es clave para definir el tipo de enlace. Siempre debe usarse uno de los siguientes valores:

        | Tipo GoJS-UML | "relationshipType" | Apariencia | Extremo 'from' | Extremo 'to' |
        | :--- | :--- | :--- | :--- | :--- |
        | **Herencia** | "Inheritance" | Línea sólida con flecha triangular vacía. | Subclase | Superclase |
        | **Realización** | "Realization" | Línea discontinua con flecha triangular vacía. | Clase que implementa | Interfaz |
        | **Composición** | "Composition" | Línea sólida con diamante sólido (negro) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (contenida) |
        | **Agregación** | "Aggregation" | Línea sólida con diamante vacío (blanco) **solo en el extremo 'from'**. **SIN MULTIPLICIDAD** | El "Todo" (contenedor) | La "Parte" (agregada) |
        | **Dependencia** | "Dependency" | Línea discontinua con flecha abierta. **relationshipName fijo: <<Dependency>>** | Clase dependiente/cliente | Clase proveedora/servicio |
        | **Uso** | "Usage" | Mismo que Dependencia, pero generalmente con 'relationshipName': "<<use>>". | Clase que usa | Recurso usado |
        | **Asociación** | "Association" | Línea sólida sin puntas especiales. Usar 'fromMultiplicity' y 'toMultiplicity' para cardinalidad. EL CAMPO "relationshipName" DEBE ESTAR PRESENTE. | Cualquiera | Cualquiera |

        **NOTA IMPORTANTE SOBRE COMPOSICIÓN Y AGREGACIÓN:**
        ➜ El rombo (lleno o vacío) **solo debe aparecer en el extremo 'from'**, nunca en ambos lados.
        ➜ Si el generador detecta rombos duplicados, debe corregir automáticamente para que solo el extremo 'from' lo conserve.

        **Nota sobre Asociación:** Si la asociación es direccional, se debe usar una plantilla GoJS con flecha para ese tipo de link, pero el relationshipType base sigue siendo "Association". El JSON solo necesita el relationshipType correcto para que GoJS aplique el estilo.

        """;;
        
    // --- Métodos Auxiliares de OpenAI ---
    private HttpHeaders createOpenAIHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }
    
    private JsonNode extractOpenAIJson(ResponseEntity<JsonNode> responseEntity) throws Exception {
        JsonNode response = responseEntity.getBody();
        if (response == null) {
            throw new IllegalStateException("Respuesta vacía de OpenAI.");
        }
        
        // Extraer el contenido JSON del campo 'content' en la estructura de OpenAI
        JsonNode messageNode = response.path("choices").get(0).path("message");
        if (messageNode.isMissingNode()) {
            throw new IllegalStateException("Estructura de respuesta inesperada de OpenAI.");
        }
        
        String jsonContent = messageNode.path("content").asText().trim();
        
        // El formato JSON debe ser limpio gracias a response_format=json_object,
        // pero se mantiene la limpieza por seguridad.
        String cleanedText = jsonContent.replaceAll("^```json\\s*|```\\s*$", "").trim();
        
        return objectMapper.readTree(cleanedText);
    }
    // ------------------------------------

    // 1. 📝 GENERAR DIAGRAMA POR TEXTO (MIGRADO A OPENAI)
    public JsonNode generarDiagrama(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("El campo 'prompt' es obligatorio.");
        }

        Map<String, Object> systemMessage = Map.of(
            "role", "system",
            "content", GOJS_PROMPT_INSTRUCTION_GENERAR
        );

        Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", prompt
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", OPENAI_MODEL);
        payload.put("messages", List.of(systemMessage, userMessage));
        payload.put("response_format", Map.of("type", "json_object")); 

        try {
            HttpHeaders headers = createOpenAIHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                OPENAI_BASE_URL, HttpMethod.POST, entity, JsonNode.class
            );

            return extractOpenAIJson(responseEntity);
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al llamar a OpenAI: {} - Respuesta: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error al llamar a la API de OpenAI: " + e.getStatusCode() +
                    ". Detalle: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error interno al generar diagrama", e);
            throw new RuntimeException("Error interno al generar diagrama", e);
        }
    }

    // 2. ✏️ MODIFICAR DIAGRAMA POR TEXTO (MIGRADO A OPENAI)
    public JsonNode modificarDiagrama(String prompt, Object jsonActual) {
        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("El campo 'prompt' es obligatorio.");
        }
        if (jsonActual == null) {
            throw new IllegalArgumentException("El campo 'jsonActual' es obligatorio.");
        }

        try {
            String combinedContent = "Prompt del usuario: " + prompt + "\nJSON actual: " +
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonActual);

            Map<String, Object> systemMessage = Map.of(
                "role", "system",
                "content", GOJS_PROMPT_INSTRUCTION_MODIFICAR
            );

            Map<String, Object> userMessage = Map.of(
                "role", "user",
                "content", combinedContent
            );

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", OPENAI_MODEL);
            payload.put("messages", List.of(systemMessage, userMessage));
            payload.put("response_format", Map.of("type", "json_object")); 
            
            HttpHeaders headers = createOpenAIHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                OPENAI_BASE_URL, HttpMethod.POST, entity, JsonNode.class
            );

            return extractOpenAIJson(responseEntity);
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al llamar a OpenAI: {} - Respuesta: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error al llamar a la API de OpenAI: " + e.getStatusCode() +
                    ". Detalle: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error interno al modificar diagrama", e);
            throw new RuntimeException("Error interno al modificar diagrama", e);
        }
    }

    // 3. 🖼️ GENERAR DESDE IMAGEN (MÉTODO ANTERIOR CON MULTIPARTFILE, MIGRADO A OPENAI)
    public JsonNode generarDesdeImagen(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("La imagen del diagrama es obligatoria.");
        }

        try {
            String base64Image = Base64.getEncoder().encodeToString(imagen.getBytes());
            String mimeType = imagen.getContentType() != null ? imagen.getContentType() : "image/jpeg";
            
            // Reutiliza la lógica del método base64 para construir el payload
            return generarDesdeImagenBase64Internal(base64Image, mimeType);

        } catch (Exception e) {
            log.error("Error interno al generar diagrama desde imagen", e);
            throw new RuntimeException("Error interno al generar diagrama desde imagen", e);
        }
    }
    
    // 4. 🖼️ GENERAR DESDE IMAGEN (MÉTODO ANTERIOR CON BASE64, MIGRADO A OPENAI)
    // El método original generarDesdeImagenBase64 se renombra a una versión interna
    public JsonNode generarDesdeImagenBase64(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("La imagen en base64 es obligatoria.");
        }
        
        // Asumiendo jpeg si no se especifica el MIME type, lo cual es arriesgado
        // pero necesario sin el contexto del MultipartFile
        String mimeType = "image/jpeg"; 
        
        try {
            return generarDesdeImagenBase64Internal(base64Image, mimeType);
        } catch (Exception e) {
            log.error("Error interno al generar diagrama desde imagen base64", e);
            throw new RuntimeException("Error interno al generar diagrama desde imagen base64", e);
        }
    }
    
    // ⚙️ Método privado unificado para llamadas de imagen a OpenAI
    private JsonNode generarDesdeImagenBase64Internal(String base64Image, String mimeType) {
        
        String url = OPENAI_BASE_URL; 
        
        Map<String, Object> systemMessage = Map.of(
            "role", "system",
            "content", GOJS_PROMPT_INSTRUCTION_IMAGEN
        );

        Map<String, Object> textPart = Map.of(
            "type", "text",
            "text", "Analiza la siguiente imagen UML y genera el modelo GoJS."
        );

        Map<String, Object> imagePart = Map.of(
            "type", "image_url",
            "image_url", Map.of(
                // Usa el mimeType correcto
                "url", "data:" + mimeType + ";base64," + base64Image 
            )
        );

        Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", List.of(textPart, imagePart)
        );
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", OPENAI_MODEL);
        payload.put("messages", List.of(systemMessage, userMessage));
        payload.put("response_format", Map.of("type", "json_object")); 


        try {
            HttpHeaders headers = createOpenAIHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, entity, JsonNode.class
            );

            return extractOpenAIJson(responseEntity);

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al llamar a OpenAI: {} - Respuesta: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error al comunicarse con la API de OpenAI: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error interno al generar diagrama desde imagen base64", e);
            throw new RuntimeException("Error interno al generar diagrama desde imagen base64", e);
        }
    }
}