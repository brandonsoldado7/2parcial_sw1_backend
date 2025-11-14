import '../models/material.dart';
import '../utils/api_client.dart';

class MaterialService {
  final _api = ApiClient();
  final _endpoint = 'material';

  /// 🔹 Obtener todos los registros
  Future<List<Material>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Material>((e) => Material.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Material> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Material.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Material item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Material item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
