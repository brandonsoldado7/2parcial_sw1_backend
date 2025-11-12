package com.segundoparcialsw1.diagramadorinteligente.service;

import freemarker.template.*;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * CodeGenFlutterService
 *
 * - Toma el JSON enriquecido (mismo que el backend) - Genera un proyecto Flutter completo
 * con: * pubspec.yaml (una sola vez) * main.dart (una sola vez) *
 * lib/models/[entidad].dart (por cada entidad con generateCRUD:true) *
 * lib/services/[entidad]_service.dart (por cada entidad) *
 * lib/screens/[entidad]_list_screen.dart (por cada entidad) *
 * lib/screens/[entidad]_detail_screen.dart (por cada entidad) *
 * lib/widgets/[entidad]_form.dart (por cada entidad) * lib/utils/constants.dart *
 * lib/utils/api_client.dart * analysis_options.yaml * .gitignore * README.md
 */

@Service
public class CodeGenFlutterService {

	private final Configuration cfg;

	public CodeGenFlutterService() throws IOException {
		cfg = new Configuration(Configuration.VERSION_2_3_32);
		
		// Usar ClassLoader para que funcione tanto en desarrollo como en producción (JAR)
		cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates/flutter");
		
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Habilitar iterables tipo JSONArray en FreeMarker
		DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32);
		owb.setIterableSupport(true);
		cfg.setObjectWrapper(owb.build());
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
	 * JSONArray -> Map/List recursivo
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
	 * ------------------------------------------------------- 🔹 Convertir nombre a
	 * snake_case: "Usuario" -> "usuario", "UsuarioRol" -> "usuario_rol"
	 * -------------------------------------------------------
	 */
	private String toSnakeCase(String camelCase) {
		return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
	}

	/*
	 * ------------------------------------------------------- 🔹 Convertir nombre a
	 * kebab-case: "Usuario" -> "usuario", "UsuarioRol" -> "usuario-rol"
	 * -------------------------------------------------------
	 */
	private String toKebabCase(String camelCase) {
		return camelCase.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
	}

	/*
	 * ------------------------------------------------------- 🔹 Convertir tipo Java a
	 * Dart: "String" -> "String", "Long" -> "int", "Boolean" -> "bool", "LocalDateTime"
	 * -> "DateTime", etc. -------------------------------------------------------
	 */
	private String javaToDartType(String javaType) {
		if (javaType == null || javaType.isEmpty())
			return "dynamic";

		switch (javaType) {
			case "String":
				return "String";
			case "int":
			case "Integer":
			case "Long":
				return "int";
			case "double":
			case "Double":
			case "float":
			case "Float":
				return "double";
			case "boolean":
			case "Boolean":
				return "bool";
			case "LocalDateTime":
			case "LocalDate":
			case "Date":
				return "DateTime";
			case "List":
			case "ArrayList":
				return "List";
			case "Map":
				return "Map";
			default:
				return "dynamic";
		}
	}

	private JSONObject findEntityByName(JSONArray entities, String name) {
		for (int i = 0; i < entities.length(); i++) {
			JSONObject e = entities.getJSONObject(i);
			if (name.equals(e.optString("name"))) {
				return e;
			}
		}
		return null;
	}

	/*
	 * ------------------------------------------------------- 🔹 Generar TODA la carpeta
	 * generated-frontend + frontend.zip
	 *
	 * Entrada: - model → el JSON enriquecido - appName → ej. "mi_app" - packageName → ej.
	 * "com.example.miapp"
	 *
	 * Salida: - frontend.zip con todo el código Flutter listo
	 * -------------------------------------------------------
	 */
	public File generateFromModel(JSONObject model, String appName, String packageName) throws Exception {

		// =========================
		// 1. Limpiar / preparar carpetas
		// =========================
		Path genDir = Paths.get("/tmp/generated-frontend");
		if (Files.exists(genDir)) {
			Files.walk(genDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}

		Path libDir = genDir.resolve("lib");
		Path modelsDir = libDir.resolve("models");
		Path servicesDir = libDir.resolve("services");
		Path screensDir = libDir.resolve("screens");
		Path widgetsDir = libDir.resolve("widgets");
		Path utilsDir = libDir.resolve("utils");

		Files.createDirectories(modelsDir);
		Files.createDirectories(servicesDir);
		Files.createDirectories(screensDir);
		Files.createDirectories(widgetsDir);
		Files.createDirectories(utilsDir);

		// =========================
		// 2. Archivos únicos (solo una vez)
		// =========================

		// pubspec.yaml
		Map<String, Object> pubspecData = new HashMap<>();
		pubspecData.put("appName", appName);
		pubspecData.put("packageName", packageName);
		renderTemplate("pubspec.yaml.ftl", pubspecData, genDir.resolve("pubspec.yaml"));

		// main.dart
		Map<String, Object> mainData = new HashMap<>();
		mainData.put("appName", appName);

		// Agrega las entidades al contexto (para el <#list> del template) del punto 4
		JSONArray entities = model.optJSONArray("entities");
		if (entities == null) {
			entities = new JSONArray();
		}
		mainData.put("entities", toFreemarkerSafe(entities));
		mainData.put("camelToSnake", new CamelToSnakeMapper());

		renderTemplate("main.dart.ftl", mainData, libDir.resolve("main.dart"));

		// analysis_options.yaml
		renderTemplate("analysis_options.yaml.ftl", new HashMap<>(), genDir.resolve("analysis_options.yaml"));

		// .gitignore
		renderTemplate("gitignore.ftl", new HashMap<>(), genDir.resolve(".gitignore"));

		// README.md
		Map<String, Object> readmeData = new HashMap<>();
		readmeData.put("appName", appName);
		renderTemplate("README.md.ftl", readmeData, genDir.resolve("README.md"));

		// =========================
		// 3. Archivos de utilidad (una sola vez)
		// =========================

		// constants.dart
		Map<String, Object> constantsData = new HashMap<>();
		constantsData.put("baseUrl", "http://192.168.0.76:8081/api"); // localhost para
																		// emulador
		renderTemplate("utils/constants.dart.ftl", constantsData, utilsDir.resolve("constants.dart"));

		// api_client.dart
		renderTemplate("utils/api_client.dart.ftl", new HashMap<>(), utilsDir.resolve("api_client.dart"));
		// json_utils.dart
		renderTemplate("utils/json_utils.dart.ftl", new HashMap<>(), utilsDir.resolve("json_utils.dart"));

		// =========================
		// 4. Procesar entidades (N veces)
		// =========================

		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);

			boolean genCrud = entity.optBoolean("generateCRUD", true);
			if (!genCrud) {
				// No generamos Flutter para entidades sin CRUD
				continue;
			}

			String className = entity.optString("name", "UnknownEntity");
			String snakeCase = toSnakeCase(className);
			// String snakeCase = className.toLowerCase();

			Map<String, Object> entityData = new HashMap<>();
			entityData.put("className", className);
			entityData.put("snakeCase", snakeCase);
			entityData.put("entity", toFreemarkerSafe(entity));
			entityData.put("javaToDartType", new JavaToDartTypeMapper());
			entityData.put("camelToSnake", new CamelToSnakeMapper());

			System.out.println("📱 Generando Flutter para entidad: " + className);

			// 🔹 Herencia segura
			if (entity.has("inheritance") && !entity.isNull("inheritance")) {
				String parentName = entity.getString("inheritance");
				entityData.put("inheritance", parentName);

				// Buscar la entidad padre y extraer sus atributos
				JSONObject parentEntity = findEntityByName(entities, parentName);
				if (parentEntity != null && parentEntity.has("attributes")) {
					JSONArray parentAttrs = parentEntity.getJSONArray("attributes");
					entityData.put("parentAttributes", toFreemarkerSafe(parentAttrs));
					// System.out.println(" aqui class: "+parentAttrs);
				}
			}

			if (entity.has("relations")) {
				entityData.put("relations", toFreemarkerSafe(entity.getJSONArray("relations")));
			}

			System.out.println("aqui entity: " + new JSONObject(entityData).toString(2));
			// 1. Model: [entity].dart
			renderTemplate("models/model.dart.ftl", entityData, modelsDir.resolve(snakeCase + ".dart"));

			// 2. Service: [entity]_service.dart
			renderTemplate("services/service.dart.ftl", entityData, servicesDir.resolve(snakeCase + "_service.dart"));

			// 3. Screen ListScreen: [entity]_list_screen.dart
			renderTemplate("screens/list_screen.dart.ftl", entityData,
					screensDir.resolve(snakeCase + "_list_screen.dart"));

			// 4. Screen DetailScreen: [entity]_detail_screen.dart
			renderTemplate("screens/detail_screen.dart.ftl", entityData,
					screensDir.resolve(snakeCase + "_detail_screen.dart"));

			// 5. Widget FormWidget: [entity]_form.dart
			renderTemplate("widgets/form.dart.ftl", entityData, widgetsDir.resolve(snakeCase + "_form.dart"));
		}

		// =========================
		// 5. Empaquetar en frontend.zip
		// =========================
		File zipFile = new File("/tmp/frontend.zip");
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

	// -------------------------------------------------------
	// 🔹 Helper para que FreeMarker pueda acceder a la función javaToDartType
	// -------------------------------------------------------
	public class JavaToDartTypeMapper implements TemplateMethodModelEx {

		@Override
		public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
			if (arguments.isEmpty())
				return "dynamic";
			String javaType = arguments.get(0).toString();
			return javaToDartType(javaType);
		}

	}

	public class CamelToSnakeMapper implements TemplateMethodModelEx {

		@Override
		public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
			if (arguments.isEmpty())
				return "";
			String input = arguments.get(0).toString();
			return toSnakeCase(input);
		}

	}

}