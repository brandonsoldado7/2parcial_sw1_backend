import '../models/${snakeCase}.dart';
import '../utils/api_client.dart';

class ${className}Service {
  final _api = ApiClient();
  final _endpoint = '${className?lower_case}';

  /// 🔹 Obtener todos los registros
  Future<List<${className}>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<${className}>((e) => ${className}.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<${className}> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return ${className}.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(${className} item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, ${className} item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
