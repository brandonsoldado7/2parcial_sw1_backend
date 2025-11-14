import '../models/reporte.dart';
import '../utils/api_client.dart';

class ReporteService {
  final _api = ApiClient();
  final _endpoint = 'reporte';

  /// 🔹 Obtener todos los registros
  Future<List<Reporte>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Reporte>((e) => Reporte.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Reporte> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Reporte.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Reporte item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Reporte item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
