import '../models/tarea.dart';
import '../utils/api_client.dart';

class TareaService {
  final _api = ApiClient();
  final _endpoint = 'tarea';

  /// 🔹 Obtener todos los registros
  Future<List<Tarea>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Tarea>((e) => Tarea.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Tarea> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Tarea.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Tarea item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Tarea item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
