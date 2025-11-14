import '../models/figura.dart';
import '../utils/api_client.dart';

class FiguraService {
  final _api = ApiClient();
  final _endpoint = 'figura';

  /// 🔹 Obtener todos los registros
  Future<List<Figura>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Figura>((e) => Figura.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Figura> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Figura.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Figura item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Figura item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
