<#noparse>
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'constants.dart';

class ApiClient {
  final _baseUrl = Constants.baseUrl;

  Future<List<dynamic>> getList(String endpoint) async {
    final response = await http.get(Uri.parse("$_baseUrl/$endpoint/listar"));
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception("Error al obtener lista: ${response.statusCode}");
    }
  }

  Future<dynamic> getById(String endpoint, int id) async {
    final response = await http.get(Uri.parse("$_baseUrl/$endpoint/$id"));
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception("Error al obtener detalle: ${response.statusCode}");
    }
  }

  Future<void> create(String endpoint, Map<String, dynamic> data) async {
    final response = await http.post(
      Uri.parse("$_baseUrl/$endpoint/guardar"),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(data),
    );
    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception("Error al crear recurso: ${response.statusCode}");
    }
  }

  Future<void> update(String endpoint, int id, Map<String, dynamic> data) async {
    final response = await http.put(
      Uri.parse("$_baseUrl/$endpoint/actualizar/$id"),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(data),
    );
    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception("Error al actualizar recurso: ${response.statusCode}");
    }
  }

  Future<void> delete(String endpoint, int id) async {
    final response = await http.delete(
      Uri.parse("$_baseUrl/$endpoint/eliminar/$id"),
    );
    if (response.statusCode != 200 && response.statusCode != 204) {
      throw Exception("Error al eliminar recurso: ${response.statusCode}");
    }
  }
}
</#noparse>
