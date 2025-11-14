import '../models/dependencia_tarea.dart';
import '../utils/api_client.dart';

class DependenciaTareaService {
  final _api = ApiClient();
  final _endpoint = 'dependenciatarea';

  /// 🔹 Obtener todos los registros
  Future<List<DependenciaTarea>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<DependenciaTarea>((e) => DependenciaTarea.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<DependenciaTarea> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return DependenciaTarea.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(DependenciaTarea item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, DependenciaTarea item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
