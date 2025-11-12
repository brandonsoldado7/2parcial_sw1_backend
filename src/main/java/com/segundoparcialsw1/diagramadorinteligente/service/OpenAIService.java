package com.segundoparcialsw1.diagramadorinteligente.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OpenAIService {

	private static final String API_URL = "https://api.openai.com/v1/chat/completions";

	private static final String MODEL = "gpt-4.1-mini"; // o gpt-4o-mini

	@Value("${openai.api.key}")
	private String apiKey;

	private final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
		.readTimeout(60, TimeUnit.SECONDS)
		.build();

	// 6️⃣ Generar script SQL de poblamiento (con IDs explícitos para todas las tablas)
	public String generateSqlPopulateFromJsonv2(String enrichedJson, int rowCount) throws Exception {
		String systemPrompt = "Eres un generador experto de scripts SQL para PostgreSQL. "
				+ "A partir del siguiente modelo JSON enriquecido (que describe entidades JPA con sus atributos y relaciones), "
				+ "genera un script SQL de INSERCIÓN DE DATOS (INSERT INTO ...) válido para PostgreSQL que cumpla con las siguientes reglas estrictas:\n\n"
				+

				"1️⃣ **Propósito:**\n" + "   - No generes CREATE TABLE ni ALTER TABLE.\n"
				+ "   - Solo genera instrucciones INSERT INTO ... VALUES ... para poblar una base existente.\n"
				+ "   - El script debe poder ejecutarse directamente en pgAdmin sin errores.\n\n" +

				"2️⃣ **Convenciones de nombres:**\n"
				+ "   - Todos los nombres de tablas y columnas deben estar en minúsculas y en formato snake_case.\n"
				+ "   - Convierte CamelCase a snake_case. Ejemplo: 'fechaRegistro' → 'fecha_registro', 'CarritoProducto' → 'carrito_producto'.\n"
				+ "   - Los nombres de claves foráneas deben terminar con '_id'. Ejemplo: 'cliente_id', 'producto_id'.\n\n"
				+

				"3️⃣ **Estructura de inserciones:**\n" + "   - Inserta " + rowCount + " filas de ejemplo por tabla.\n"
				+ "   - Todas las tablas deben tener una columna 'id' y se debe incluir en cada inserción.\n"
				+ "   - Los valores de 'id' deben comenzar en 1 y aumentar secuencialmente (1, 2, 3, ...).\n"
				+ "   - Inserta primero las tablas simples (sin claves foráneas) y luego las dependientes, respetando las relaciones.\n"
				+ "   - Las claves foráneas deben referenciar IDs válidos existentes en las tablas padres.\n\n" +

				"4️⃣ **Valores generados:**\n" + "   - Usa valores coherentes y variados según el tipo:\n"
				+ "       * String → nombres, correos o textos realistas.\n"
				+ "       * int/double → valores numéricos razonables.\n"
				+ "       * boolean → alterna entre true/false.\n"
				+ "       * Date → fechas válidas entre 2015 y 2025 (formato 'YYYY-MM-DD').\n"
				+ "   - No dejes columnas NULL a menos que la relación sea opcional.\n\n" +

				"5️⃣ **Formato del resultado:**\n"
				+ "   - Devuelve solo SQL puro (sin CREATE TABLE ni comentarios explicativos).\n"
				+ "   - Cada tabla debe tener su bloque INSERT propio.\n"
				+ "   - Incluye punto y coma ';' al final de cada bloque.\n" + "   - Ejemplo del formato esperado:\n\n"
				+ "INSERT INTO usuario (id, email, activo, nombre) VALUES\n"
				+ "(1, 'usuario1@example.com', true, 'Juan Perez'),\n"
				+ "(2, 'usuario2@example.com', false, 'Ana Ruiz'),\n"
				+ "(3, 'usuario3@example.com', true, 'Carlos Diaz'),\n"
				+ "(4, 'usuario4@example.com', true, 'Maria Lopez');\n\n"
				+ "INSERT INTO cliente (id, direccion, fecha_registro, telefono, usuario_id) VALUES\n"
				+ "(1, 'Av. Central 123', '2021-04-12', 7654321, 1),\n"
				+ "(2, 'Calle Sur 45', '2020-08-19', 7891234, 2),\n"
				+ "(3, 'Av. Norte 88', '2022-01-10', 6549870, 3),\n"
				+ "(4, 'Av. Libertad 56', '2019-09-05', 7412589, 4);\n\n"
				+ "Genera el script completo siguiendo estas reglas para todas las entidades del JSON, incluyendo herencias, composiciones y agregaciones, "
				+ "asegurando que todas las tablas tengan su campo 'id' y que las claves foráneas usen IDs válidos.";

		return callOpenAi(systemPrompt, enrichedJson);
	}

	// 5️⃣ Interpretar modelo UML (modo inteligente)
	public String interpretModelv5(String umlJson) throws Exception {
		String systemPrompt = "Eres un analizador experto de diagramas UML para generación automática de código Spring Boot y Flutter. "
				+ "Convierte el siguiente JSON UML en un modelo semántico estructurado y coherente. "
				+ "Devuelve SIEMPRE un JSON válido con la estructura exacta: { \"entities\": [], \"dtos\": [], \"endpointsHints\": [] }. "
				+ "Cada entidad debe incluir: name, attributes [{name,type,isId}], relations [{type,target,mappedBy,owningSide,optional,jsonIgnore}], "
				+ "y generateCRUD:true (todas las clases, sin excepción, deben generar endpoints REST y pantallas CRUD). "
				+ "IMPORTANTE: si un atributo tiene isId:true, su 'type' DEBE ser 'Long' (ignora el tipo original del UML) para permitir autoincremento con JPA. "
				+ "Usa nombres en CamelCase, tipos válidos para Java, y evita relaciones circulares (usa jsonIgnore:true en un solo lado). "
				+ "Incluye todas las relaciones del UML sin omitir ninguna. "
				+ "Traduce tipos UML a relaciones JPA así: "

				+ " - inheritance → hereda de target. "
				+ "   El PADRE debe tener 'hasSubclasses': true y 'subclasses': ['Hija1','Hija2'], "
				+ "   pero **NO debe tener 'inheritance' ni ningún valor en ese campo (debe ser null o no existir).**"
				+ "   Las CLASES HIJAS deben incluir 'inheritance': 'NombrePadre'. "
				+ "   Tanto el padre como las hijas deben tener generateCRUD:true. "

				+ " - association → ManyToOne (dueño), OneToMany (inverso) o OneToOne según la cardinalidad. "
				+ " - composition → OneToMany con cascade=ALL y orphanRemoval=true. "
				+ " - aggregation → OneToMany con cascade=PERSIST. "
				+ " - dependency → vínculo lógico no persistente (usa 'dependency': true, pero aún así genera la entidad). "
				+ "Si 'relationshipType' es 'Association', no uses ni 'composition' ni 'aggregation'; "
				+ "debes generar solo ManyToOne, OneToMany o OneToOne según la cardinalidad indicada. "
				+ "En composition y aggregation: 'from' representa la clase HIJA y 'to' representa la clase PADRE. "
				+ "Ambos lados deben marcarse con los flags adecuados: "
				+ "el PADRE lleva 'composition:true' o 'aggregation:true', y el HIJO lleva 'composition_inversed:true' o 'aggregation_inversed:true'. "
				+ "Además, el lado inverso (hijo) debe tener jsonIgnore:true para evitar ciclos. "
				+ "Si la dirección del diagrama está invertida, corrígela automáticamente manteniendo esta semántica. "
				+ "Refleja bien las cardinalidades (1:1, 1:*, *:*) con owningSide:true solo en un lado. "
				+ "Para relaciones *:* (muchos a muchos), NO uses ManyToMany directo: crea una nueva ENTIDAD INTERMEDIA separada con su propio 'id' y relaciones ManyToOne hacia ambas clases. "
				+ "Esa entidad intermedia también debe tener generateCRUD:true y comportarse como una entidad normal. "
				+ "Todas las entidades deben ser consistentes, sin referencias huérfanas, y el JSON final debe ser completamente válido y parseable."
				+ "Además, antes de devolver el JSON final, asegúrate de reemplazar en todos los nombres de clases, atributos y relaciones "
				+ "las letras 'ñ' o 'Ñ' por 'n' o 'N' respectivamente. "
				+ "Por ejemplo: 'AñoPublicacion' debe transformarse en 'AnioPublicacion'. "
				+ "Aplica esta regla de forma consistente en los campos 'name', 'attributes.name' y 'relations.target'. "
				+ "Ningún identificador del JSON final debe contener la letra 'ñ' o 'Ñ'.";
		return callOpenAi(systemPrompt, umlJson);
	}

	// 🔹 Método genérico para llamar a la API
	private String callOpenAi(String systemPrompt, String userContent) throws Exception {
		JSONObject body = new JSONObject().put("model", MODEL)
			.put("messages", new JSONArray().put(new JSONObject().put("role", "system").put("content", systemPrompt))
				.put(new JSONObject().put("role", "user").put("content", userContent)));

		Request request = new Request.Builder().url(API_URL)
			.post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
			.addHeader("Authorization", "Bearer " + apiKey)
			.build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new RuntimeException("Error API: " + response);
			}
			JSONObject jsonResponse = new JSONObject(response.body().string());
			return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
		}
	}

}
