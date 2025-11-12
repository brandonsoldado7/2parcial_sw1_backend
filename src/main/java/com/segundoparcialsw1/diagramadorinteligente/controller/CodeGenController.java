package com.segundoparcialsw1.diagramadorinteligente.controller;

import com.segundoparcialsw1.diagramadorinteligente.service.CodeGenFlutterService;
import com.segundoparcialsw1.diagramadorinteligente.service.CodeGenService;
import com.segundoparcialsw1.diagramadorinteligente.service.OpenAIService;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@RestController
@RequestMapping("/api/codegen")
public class CodeGenController {

	private final OpenAIService openAIService;

	private final CodeGenService codeGenService;

	private final CodeGenFlutterService codeGenFlutterService;

	public CodeGenController(OpenAIService openAIService, CodeGenService codeGenService,
			CodeGenFlutterService codeGenFlutterService) {
		this.openAIService = openAIService;
		this.codeGenService = codeGenService;
		this.codeGenFlutterService = codeGenFlutterService;
	}

	// -------------------------BACKEND-----------------------------------
	@PostMapping("/generate-enriquesido-v2")
	public ResponseEntity<Object> generateEnriquesidov2(@RequestBody String umlJson) throws Exception {
		// Paso 1: Interpretar el UML con la IA
		String enrichedJson = openAIService.interpretModelv5(umlJson);

		// 🧠 Limpieza: a veces OpenAI devuelve ```json ... ```
		enrichedJson = enrichedJson.replaceAll("```json", "").replaceAll("```", "").trim();

		// Mostrar en consola para debug

		// Paso 2: Intentar convertirlo a JSON
		try {
			JSONObject model = new JSONObject(enrichedJson);
			// Devuelve el objeto limpio (para ver en Postman)
			return ResponseEntity.ok(model.toMap());
		}
		catch (Exception e) {
			// Si algo falla, devolvemos el texto crudo
			System.err.println("⚠️ Error parseando JSON enriquecido: " + e.getMessage());
			return ResponseEntity.ok(Map.of("rawResponse", enrichedJson, "error", e.getMessage()));
		}
	}

	// 5️⃣ Generar backend inteligente (CRUD + DTO + Relaciones)
	@PostMapping("/generate-backend-v2")
	public ResponseEntity<FileSystemResource> generateBackendV2(@RequestBody String umlJson) throws Exception {
		// Paso 1: Interpretar el UML con la IA
		String enrichedJson = openAIService.interpretModelv5(umlJson);
		// Limpieza de formato si la IA devuelve ```json o ```
		enrichedJson = enrichedJson.replaceAll("```json", "").replaceAll("```", "").trim();

		// Paso 2: Crear el objeto JSON y asegurar campos mínimos
		JSONObject model = new JSONObject(enrichedJson);

		// Garantizar consistencia: agregar arrays vacíos si faltan
		if (!model.has("dtos"))
			model.put("dtos", new org.json.JSONArray());
		if (!model.has("endpointsHints"))
			model.put("endpointsHints", new org.json.JSONArray());

		// Paso 3: Generar el backend completo con FreeMarker
		File zipFile = codeGenService.generateFromModel(model, "com.example.demo");

		// Paso 4: Devolver el ZIP como descarga
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("backend.zip").build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<>(new FileSystemResource(zipFile), headers, HttpStatus.OK);
	}

	// ========== FRONTEND FLUTTER ==========

	/**
	 * Genera solo el proyecto Flutter desde el UML
	 */
	@PostMapping("/generate-flutter-v2")
	public ResponseEntity<FileSystemResource> generateFlutterV2(@RequestBody String umlJson,
			@RequestParam(defaultValue = "my_app") String appName,
			@RequestParam(defaultValue = "com.example.myapp") String packageName) throws Exception {

		// Paso 1: Interpretar el UML con la IA
		String enrichedJson = openAIService.interpretModelv5(umlJson);
		// Limpieza de formato
		enrichedJson = enrichedJson.replaceAll("```json", "").replaceAll("```", "").trim();

		// Paso 2: Crear el objeto JSON
		JSONObject model = new JSONObject(enrichedJson);

		// Garantizar consistencia
		if (!model.has("entities"))
			model.put("entities", new org.json.JSONArray());

		// Paso 3: Generar el frontend Flutter
		File zipFile = codeGenFlutterService.generateFromModel(model, appName, packageName);

		// Paso 4: Devolver el ZIP
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("frontend.zip").build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<>(new FileSystemResource(zipFile), headers, HttpStatus.OK);
	}

	/**
	 * Genera Backend + Frontend + script SQL en una sola petición (un solo zip final)
	 */
	@PostMapping("/generate-full-v2")
	public ResponseEntity<FileSystemResource> generateFullV2(@RequestBody String umlJson,
			@RequestParam(defaultValue = "my_app") String appName,
			@RequestParam(defaultValue = "com.example.myapp") String packageName,
			@RequestParam(required = false) Integer rows) throws Exception {

		// Paso 1️⃣: Interpretar el UML enriquecido con IA
		String enrichedJson = openAIService.interpretModelv5(umlJson);
		enrichedJson = enrichedJson.replaceAll("```json", "").replaceAll("```", "").trim();

		// Paso 2️⃣: Crear el objeto JSON y asegurar consistencia mínima
		JSONObject model = new JSONObject(enrichedJson);
		if (!model.has("dtos"))
			model.put("dtos", new org.json.JSONArray());
		if (!model.has("endpointsHints"))
			model.put("endpointsHints", new org.json.JSONArray());
		if (!model.has("entities"))
			model.put("entities", new org.json.JSONArray());

		// Paso 3️⃣: Configurar estructura de carpetas
		File rootDir = new File("generated");
		// 🧹 Limpieza previa de carpetas
		if (rootDir.exists()) {
			deleteDirectoryRecursively(rootDir);
		}
		rootDir.mkdirs();

		if (!rootDir.exists())
			rootDir.mkdirs();

		File backendDir = new File(rootDir, "backend");
		File frontendDir = new File(rootDir, "frontend");
		backendDir.mkdirs();
		frontendDir.mkdirs();

		// Paso 4️⃣: Generar Backend y Frontend
		System.out.println("🔧 Generando Backend...");
		File backendZip = codeGenService.generateFromModel(model, "com.example.demo");

		System.out.println("📱 Generando Frontend Flutter...");
		File frontendZip = codeGenFlutterService.generateFromModel(model, appName, packageName);

		// Extraer zips generados en carpetas respectivas
		codeGenService.unzipToDirectory(backendZip, backendDir);
		codeGenService.unzipToDirectory(frontendZip, frontendDir);

		// Paso 5️⃣: Generar script SQL (poblamiento)
		int rowCount = (rows != null && rows > 0) ? rows : 4;
		System.out.println("🗃️ Generando script SQL de poblamiento (" + rowCount + " filas por tabla)...");
		String sqlScript = openAIService.generateSqlPopulateFromJsonv2(enrichedJson, rowCount);
		File sqlFile = new File(rootDir, "populate.sql");
		try (FileWriter writer = new FileWriter(sqlFile)) {
			writer.write(sqlScript);
		}

		// Paso 6️⃣: Crear ZIP final con todo el contenido
		File finalZip = codeGenService.zipDirectory(rootDir, "full_project.zip");

		// Limpiar los ZIP intermedios
		backendZip.delete();
		frontendZip.delete();

		// Paso 7️⃣: Devolver el ZIP completo como descarga
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("full_project.zip").build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		System.out.println("✅ Proyecto completo generado: " + finalZip.getAbsolutePath());
		return new ResponseEntity<>(new FileSystemResource(finalZip), headers, HttpStatus.OK);
	}

	@PostMapping("/generate-sql-populate")
	public ResponseEntity<FileSystemResource> generateSqlPopulate(@RequestBody String umlJson,
			@RequestParam(required = false) Integer rows) throws Exception {

		// 1️⃣ Interpretar el UML enriquecido con IA
		String enrichedJson = openAIService.interpretModelv5(umlJson);
		enrichedJson = enrichedJson.replaceAll("```json", "").replaceAll("```", "").trim();

		// 2️⃣ Determinar filas (por defecto 4)
		int rowCount = (rows != null && rows > 0) ? rows : 10;

		// 3️⃣ Generar script SQL (usa el nuevo método del servicio)
		String sqlScript = openAIService.generateSqlPopulateFromJsonv2(enrichedJson, rowCount);

		// 4️⃣ Guardar archivo
		File sqlFile = codeGenService.saveTextAsFile(sqlScript, "populate.sql");

		// 5️⃣ Devolver como descarga
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename("populate.sql").build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<>(new FileSystemResource(sqlFile), headers, HttpStatus.OK);
	}

	private void deleteDirectoryRecursively(File dir) {
		if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				deleteDirectoryRecursively(file);
			}
		}
		dir.delete();
	}

}
