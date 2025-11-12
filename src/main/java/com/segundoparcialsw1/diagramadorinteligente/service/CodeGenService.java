package com.segundoparcialsw1.diagramadorinteligente.service;

import freemarker.template.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.zip.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * CodeGenServiceV2
 *
 * - Toma el JSON enriquecido (interpretModelv5/v4 con todas las reglas) - Normaliza
 * entidades y relaciones (IDs Long, herencia, composición, etc.) - Genera: * Entities JPA
 * * Repository * Service * Controller * DTOs (si existen) * README * pom.xml = en
 * backend.zip
 *
 * Importante: - TODAS las entidades con generateCRUD:true generan CRUD REST propio,
 * incluso las tablas intermedias de relaciones *:*.
 *
 * - Se respetan flags: composition / composition_inversed aggregation /
 * aggregation_inversed dependency jsonIgnore
 *
 * - Se respeta herencia: inheritance, hasSubclasses, subclasses
 */

@Service
public class CodeGenService {

	private final Configuration cfg;

	public CodeGenService() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_32);
		cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates/sprintboot");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Habilitar iterables tipo JSONArray en FreeMarker
		DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
		owb.setIterableSupport(true);
		cfg.setObjectWrapper(owb.build());
	}

	public File saveTextAsFile(String content, String fileName) throws IOException {
		File dir = new File("/tmp/generatedSQL");
		if (!dir.exists())
			dir.mkdirs();

		File outputFile = new File(dir, fileName);
		try (FileWriter writer = new FileWriter(outputFile)) {
			writer.write(content);
		}

		System.out.println("💾 Archivo generado: " + outputFile.getAbsolutePath());
		return outputFile;
	}

	public void unzipToDirectory(File zipFile, File targetDir) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File newFile = new File(targetDir, entry.getName());
				if (entry.isDirectory()) {
					newFile.mkdirs();
				}
				else {
					new File(newFile.getParent()).mkdirs();
					try (FileOutputStream fos = new FileOutputStream(newFile)) {
						zis.transferTo(fos);
					}
				}
			}
		}
	}

	public File zipDirectory(File folder, String zipName) throws IOException {
		File zipFile = new File(zipName);
		try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipFolderRecursive(folder, folder.getName(), zos);
		}
		return zipFile;
	}

	private void zipFolderRecursive(File folder, String parentPath, ZipOutputStream zos) throws IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipFolderRecursive(file, parentPath + "/" + file.getName(), zos);
			}
			else {
				try (FileInputStream fis = new FileInputStream(file)) {
					ZipEntry zipEntry = new ZipEntry(parentPath + "/" + file.getName());
					zos.putNextEntry(zipEntry);
					fis.transferTo(zos);
					zos.closeEntry();
				}
			}
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Utilidad: Renderizar una
	 * plantilla FreeMarker a archivo
	 * -------------------------------------------------------
	 */

	private void renderTemplate(String templateName, Map<String, Object> data, Path outputPath) throws IOException {
		try (Writer out = new FileWriter(outputPath.toFile())) {
			Template template = cfg.getTemplate(templateName);
			template.process(data, out);
		}
		catch (TemplateException e) {
			throw new IOException("Error procesando template: " + templateName, e);
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Utilidad: JSONObject /
	 * JSONArray -> Map/List recursivo (FreeMarker no ama org.json directamente)
	 * -------------------------------------------------------
	 */
	private Object toFreemarkerSafe(Object json) {
		if (json instanceof JSONObject obj) {
			Map<String, Object> map = new LinkedHashMap<>();
			for (String key : obj.keySet()) {
				map.put(key, toFreemarkerSafe(obj.get(key)));
			}
			return map;
		}
		else if (json instanceof JSONArray arr) {
			List<Object> list = new ArrayList<>();
			for (int i = 0; i < arr.length(); i++) {
				list.add(toFreemarkerSafe(arr.get(i)));
			}
			return list;
		}
		else {
			return json;
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Regla global: si
	 * isId:true => type = "Long" (aunque venga int, String, etc. del UML)
	 * -------------------------------------------------------
	 */
	private void forceLongIds(JSONObject entity) {
		JSONArray attrs = entity.optJSONArray("attributes");
		if (attrs == null)
			return;

		for (int i = 0; i < attrs.length(); i++) {
			JSONObject att = attrs.getJSONObject(i);
			boolean isId = att.optBoolean("isId", false);
			if (isId) {
				att.put("type", "Long"); // forzamos compatibilidad con @GeneratedValue
			}
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Limpieza herencia: - Si
	 * tiene "inheritance": Padre => marcar extends = Padre - Si una entidad menciona
	 * subclasses => hasSubclasses: true - Asegurar "extends" en todas las entidades (NULL
	 * si no aplica) -------------------------------------------------------
	 */
	private void normalizeInheritance(JSONObject entity, JSONArray allEntities) {
		String parent = entity.optString("inheritance", null);
		if (parent != null && !parent.isEmpty() && !"Padre".equals(parent)) {
			// Esta entidad es una subclase
			entity.put("extends", parent);
			entity.put("isSubclass", true);
		}
		else {
			// No hereda
			entity.put("extends", JSONObject.NULL);
			entity.put("isSubclass", false);
		}

		// Si una entidad tiene "subclasses", márkala como base
		if (entity.has("subclasses")) {
			entity.put("hasSubclasses", true);
		}
		else {
			// Si otra entidad la referencia como padre, también cuenta
			String thisName = entity.optString("name", "");
			boolean isParentOfSomeone = false;
			for (int i = 0; i < allEntities.length(); i++) {
				JSONObject other = allEntities.getJSONObject(i);
				String otherParent = other.optString("inheritance", "");
				if (thisName.equals(otherParent)) {
					isParentOfSomeone = true;
					break;
				}
			}
			entity.put("hasSubclasses", isParentOfSomeone);
			if (isParentOfSomeone) {
				// construir lista subclasses si no vino
				JSONArray subs = new JSONArray();
				for (int i = 0; i < allEntities.length(); i++) {
					JSONObject other = allEntities.getJSONObject(i);
					String otherParent = other.optString("inheritance", "");
					if (thisName.equals(otherParent)) {
						subs.put(other.optString("name"));
					}
				}
				entity.put("subclasses", subs);
			}
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Normalizar una sola
	 * relación y enriquecerla Maneja: - association / composition / aggregation /
	 * dependency - OneToOne / OneToMany / ManyToOne / ManyToMany - jsonIgnore y
	 * owningSide
	 *
	 * IMPORTANTE: - composition / aggregation: * Queremos ambos flags claros: - Padre
	 * (contenedor): composition:true / aggregation:true - Hijo (parte):
	 * composition_inversed:true / aggregation_inversed:true (Tu última regla pide
	 * exactamente eso)
	 *
	 * - dependency: modelamos como relación lógica no persistente (marcar "dependency":
	 * true)
	 *
	 * - Association: NO debe inventar composition/aggregation
	 *
	 * Retorna una nueva relación "relOut" lista para plantillas.
	 * -------------------------------------------------------
	 */
	private JSONObject normalizeSingleRelation(JSONObject rel, String currentEntityName) {

		JSONObject relOut = new JSONObject();

		// copiamos campos "básicos"
		for (String k : rel.keySet()) {
			relOut.put(k, rel.get(k));
		}

		String rawType = rel.optString("type", "");
		String lowerType = rawType.toLowerCase(Locale.ROOT);

		// Campos comunes garantizados
		relOut.put("target", rel.optString("target", ""));
		relOut.put("owningSide", rel.optBoolean("owningSide", true));
		relOut.put("optional", rel.optBoolean("optional", true));
		relOut.put("jsonIgnore", rel.optBoolean("jsonIgnore", false));

		// mappedBy puede venir null, '', etc.
		if (!rel.has("mappedBy")) {
			relOut.put("mappedBy", JSONObject.NULL);
		}
		else {
			String mb = rel.optString("mappedBy", "");
			relOut.put("mappedBy", mb.isEmpty() ? JSONObject.NULL : mb);
		}

		// --- HERENCIA (por si viniera como relación declarada, normalmente no) ---
		if ("inheritance".equals(lowerType)) {
			relOut.put("jpaType", "inheritance");
			return relOut;
		}

		// --- DEPENDENCY ---
		if ("dependency".equals(lowerType)) {
			// marcamos como no persistente
			relOut.put("jpaType", "Transient");
			relOut.put("dependency", true);
			relOut.put("comment", "// Dependencia lógica (no persistente) con " + relOut.optString("target"));
			return relOut;
		}

		// --- ASSOCIATION ---
		// Regla: no marcar composition / aggregation en associations
		if ("association".equals(lowerType)) {
			// decidir jpaType según cardinalidad que ya vino:
			// si el JSON enriquecido ya nos dio "type":
			// "OneToOne"/"OneToMany"/"ManyToOne" entonces lo usamos
			// si no, default ManyToOne (lado dueño típico)
			// Aquí "rawType" será "association", así que necesitamos JPA real.
			// Asumimos que ya entró como algo concreto en tu salida final (tu modelo ya
			// pone OneToMany, ManyToOne, etc.)
			relOut.put("jpaType", guessJpaFromRelation(rel));
			// NO agregamos "composition", "aggregation"
			return relOut;
		}

		// --- COMPOSITION / AGGREGATION ---
		// Estas usan tus banderas especiales. Las relaciones ya vienen marcadas en tu
		// JSON final como:
		// - En padre: composition:true o aggregation:true
		// - En hijo: composition_inversed:true / aggregation_inversed:true
		//
		// A la hora de generar la entidad JPA:
		// composition => OneToMany cascade=ALL orphanRemoval=true
		// aggregation => OneToMany cascade=PERSIST
		//
		// Nota: En tu JSON final actual, "type" ya viene como "OneToMany" o "ManyToOne",
		// y además vienen las banderas composition / composition_inversed / aggregation /
		// aggregation_inversed
		// Entonces básicamente solo tenemos que trasladar esa info a relOut.

		if ("composition".equals(lowerType)) {
			// viejo flujo (por si llegara así crudo). Hoy casi nunca viene así porque ya
			// traes OneToMany.
			relOut.put("jpaType", "OneToMany");
			relOut.put("cascade", "CascadeType.ALL");
			relOut.put("orphanRemoval", true);
			return relOut;
		}

		if ("aggregation".equals(lowerType)) {
			// idem: hoy ya viene traducido, pero lo dejamos por coherencia.
			relOut.put("jpaType", "OneToMany");
			relOut.put("cascade", "CascadeType.PERSIST");
			return relOut;
		}

		// --- RELACIÓN YA EN TERMINOS JPA (OneToOne, OneToMany, ManyToOne, ManyToMany)
		// ---
		switch (lowerType) {
			case "onetoone" -> {
				relOut.put("jpaType", "OneToOne");
			}
			case "onetomany" -> {
				relOut.put("jpaType", "OneToMany");
			}
			case "manytoone" -> {
				relOut.put("jpaType", "ManyToOne");
			}
			case "manytomany" -> {
				// OJO: en nuestro modelo actual NO queremos ManyToMany directo.
				// Pero si llegara algo así crudo lo vamos a marcar.
				relOut.put("jpaType", "ManyToMany");
			}
			default -> {
				// fallback
				relOut.put("jpaType", guessJpaFromRelation(rel));
			}
		}

		// 📝 Enriquecimientos automáticos:
		// Inferir mappedBy si falta, cuando es lado inverso (OneToMany o OneToOne
		// inverso)
		String jpaType = relOut.optString("jpaType", "");
		boolean owningSide = relOut.optBoolean("owningSide", true);

		if ((jpaType.equals("OneToMany") || (jpaType.equals("OneToOne") && !owningSide))
				&& (relOut.isNull("mappedBy") || relOut.optString("mappedBy", "").isEmpty())) {

			String inferred = lowerFirst(currentEntityName);
			relOut.put("mappedBy", inferred);
		}

		// Quitar mappedBy del lado dueño en ManyToOne / OneToOne dueño
		if (jpaType.equals("ManyToOne") || (jpaType.equals("OneToOne") && owningSide)) {
			relOut.put("mappedBy", JSONObject.NULL);
		}

		return relOut;
	}

	/*
	 * ------------------------------------------------------- 🔹 Helper: intenta deducir
	 * jpaType si la relación es ambigua
	 * -------------------------------------------------------
	 */
	private String guessJpaFromRelation(JSONObject rel) {
		// Si ya viene "type": "OneToMany", úsalo.
		// Si ya viene "type": "ManyToOne", úsalo.
		// etc.
		String t = rel.optString("type", "");
		switch (t) {
			case "OneToOne":
				return "OneToOne";
			case "OneToMany":
				return "OneToMany";
			case "ManyToOne":
				return "ManyToOne";
			case "ManyToMany":
				return "ManyToMany"; // normalmente deberíamos no usar directo, pero lo
										// dejamos
			default:
				return "ManyToOne";
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 lowerFirst("Carrito") =>
	 * "carrito" -------------------------------------------------------
	 */
	private String lowerFirst(String s) {
		if (s == null || s.isEmpty())
			return s;
		if (s.length() == 1)
			return s.toLowerCase(Locale.ROOT);
		return s.substring(0, 1).toLowerCase(Locale.ROOT) + s.substring(1);
	}

	/*
	 * ------------------------------------------------------- 🔹 Normalizar TODAS las
	 * relaciones de una entidad
	 *
	 * Hace: - Recorre cada relación declarada - Aplica normalizeSingleRelation() - Vuelve
	 * a colocar el array "relations" enriquecido
	 *
	 * También: - Marca isJoinEntity si detectas que es una entidad puente "manual" (pero
	 * OJO: ahora tu modelo siempre pone un único id Long en TODAS, incluidas las
	 * intermedias, porque tus *:* ya se vuelven entidades normales con id propio. Eso es
	 * perfecto → no necesitas ID compuesto).
	 *
	 * -> Igual dejamos la marca isJoinEntity si quieres identificar esas tablas
	 * intermedias para después darles un trato especial en front o docs.
	 * -------------------------------------------------------
	 */
	private void normalizeEntityRelations(JSONObject entity, JSONArray allEntities) {
		JSONArray rels = entity.optJSONArray("relations");
		JSONArray normalizedRels = new JSONArray();
		String entityName = entity.optString("name", "UnknownEntity");

		if (rels != null) {
			for (int i = 0; i < rels.length(); i++) {
				JSONObject rawRel = rels.getJSONObject(i);
				JSONObject cleanRel = normalizeSingleRelation(rawRel, entityName);
				normalizedRels.put(cleanRel);
			}
		}

		entity.put("relations", normalizedRels);

		// Marcar si esta entidad "parece" intermedia por convención:
		// Regla nueva en tu pipeline:
		// - Relaciones *:* ahora se vuelven entidad separada con su propio id Long
		// - generateCRUD SIEMPRE true
		// Podemos marcar heurísticamente como "joinEntity" si:
		// - tiene 2+ relaciones ManyToOne hacia otras entidades,
		// - y pocos atributos propios.
		int manyToOneCount = 0;
		if (normalizedRels != null) {
			for (int i = 0; i < normalizedRels.length(); i++) {
				JSONObject r = normalizedRels.getJSONObject(i);
				if ("ManyToOne".equals(r.optString("jpaType", ""))) {
					manyToOneCount++;
				}
			}
		}
		if (manyToOneCount >= 2) {
			entity.put("isJoinEntity", true);
		}
		else {
			entity.put("isJoinEntity", false);
		}

		// Si es joinEntity, forzamos generateCRUD:true (porque Flutter la va a consumir
		// directo)
		if (entity.optBoolean("isJoinEntity", false)) {
			entity.put("generateCRUD", true);
		}
	}

	/*
	 * ------------------------------------------------------- 🔹 Normalización completa
	 * de una entidad: - IDs -> Long - Herencia - Relaciones
	 * -------------------------------------------------------
	 */
	private JSONObject normalizeEntity(JSONObject entity, JSONArray allEntities) {

		// 1. IDs a Long
		forceLongIds(entity);

		// 2. Herencia / subclasses
		normalizeInheritance(entity, allEntities);

		// 3. Relaciones enriquecidas
		normalizeEntityRelations(entity, allEntities);

		return entity;
	}

	/*
	 * ------------------------------------------------------- 🔹 Generar TODA la carpeta
	 * generated-backend + backend.zip
	 *
	 * Entrada: - model → el JSON enriquecido que viene de interpretModelv5 - basePackage
	 * → ej. "com.generated.backend"
	 *
	 * Salida: - backend.zip con todo el código listo
	 * -------------------------------------------------------
	 */
	public File generateFromModel(JSONObject model, String basePackage) throws Exception {

		// =========================
		// 1. Limpiar / preparar carpetas
		// =========================
		Path genDir = Paths.get("/tmp/generated-backend");
		if (Files.exists(genDir)) {
			Files.walk(genDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}

		Path baseDir = genDir.resolve("src/main/java/" + basePackage.replace(".", "/"));
		Path modelDir = baseDir.resolve("model");
		Path repoDir = baseDir.resolve("repository");
		Path serviceDir = baseDir.resolve("service");
		Path controllerDir = baseDir.resolve("controller");
		Path dtoDir = baseDir.resolve("dto");
		Path resourcesDir = genDir.resolve("src/main/resources");

		Files.createDirectories(modelDir);
		Files.createDirectories(repoDir);
		Files.createDirectories(serviceDir);
		Files.createDirectories(controllerDir);
		Files.createDirectories(dtoDir);
		Files.createDirectories(resourcesDir);

		// =========================
		// 2. Archivos base
		// =========================
		renderTemplate("application.properties.ftl",
				Map.of("dbName", "mydb", "dbUser", "postgres", "dbPassword", "8960406"),
				resourcesDir.resolve("application.properties"));

		renderTemplate("GeneratedBackendApplication.ftl", Map.of("basePackage", basePackage),
				baseDir.resolve("GeneratedBackendApplication.java"));

		renderTemplate("pom.ftl", Map.of("basePackage", basePackage, "artifactId", "generated-backend"),
				genDir.resolve("pom.xml"));

		// =========================
		// 3. Procesar entidades
		// =========================
		JSONArray entities = model.optJSONArray("entities");
		if (entities == null) {
			entities = new JSONArray();
		}

		// Normalizamos TODAS primero (porque herencia y relaciones cruzadas necesitan
		// info global)
		List<JSONObject> normalizedEntityList = new ArrayList<>();
		for (int i = 0; i < entities.length(); i++) {
			JSONObject rawEntity = entities.getJSONObject(i);
			JSONObject normEntity = normalizeEntity(rawEntity, entities);
			normalizedEntityList.add(normEntity);
		}

		// Ahora generamos archivos para cada entidad
		for (JSONObject entity : normalizedEntityList) {
			String className = entity.optString("name", "UnknownEntity");

			Map<String, Object> data = new HashMap<>();
			data.put("basePackage", basePackage);
			data.put("entity", toFreemarkerSafe(entity));

			// Debug opcional en consola
			System.out.println("===== ENTITY NORMALIZADA: " + className + " =====");
			System.out.println(entity.toString(4));
			System.out.println("===============================================");

			// Entity.java
			renderTemplate("Entity.ftl", data, modelDir.resolve(className + ".java"));

			// Repository.java
			renderTemplate("Repository.ftl",
					Map.of("basePackage", basePackage, "className", className, "entity", toFreemarkerSafe(entity)),
					repoDir.resolve(className + "Repository.java"));

			// Service.java y Controller.java (solo si generateCRUD:true)
			boolean genCrud = entity.optBoolean("generateCRUD", true);
			if (genCrud) {
				renderTemplate("Service.ftl",
						Map.of("basePackage", basePackage, "className", className, "entity", toFreemarkerSafe(entity)),
						serviceDir.resolve(className + "Service.java"));

				renderTemplate("Controller.ftl",
						Map.of("basePackage", basePackage, "className", className, "entity", toFreemarkerSafe(entity),
								"endpointsHints", toFreemarkerSafe(model.optJSONArray("endpointsHints"))),
						controllerDir.resolve(className + "Controller.java"));
			}
		}

		// =========================
		// 4. DTOs si existen
		// =========================
		JSONArray dtos = model.optJSONArray("dtos");
		if (dtos != null) {
			for (int i = 0; i < dtos.length(); i++) {
				JSONObject dto = dtos.getJSONObject(i);
				renderTemplate("DTO.ftl", Map.of("basePackage", basePackage, "dto", toFreemarkerSafe(dto)),
						dtoDir.resolve(dto.getString("name") + ".java"));
			}
		}

		// =========================
		// 5. README resumen del modelo
		// =========================
		renderTemplate("README.md.ftl", Map.of("basePackage", basePackage, "entities", toFreemarkerSafe(entities)),
				genDir.resolve("README.md"));

		// =========================
		// 6. Empaquetar en backend.zip
		// =========================
		File zipFile = new File("/tmp/backend.zip");
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
			Files.walk(genDir).filter(Files::isRegularFile).forEach(path -> {
				try {
					zos.putNextEntry(new ZipEntry(genDir.relativize(path).toString()));
					Files.copy(path, zos);
					zos.closeEntry();
				}
				catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
		}

		return zipFile;
	}

}
