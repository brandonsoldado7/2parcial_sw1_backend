import '../models/accesorio.dart';
import '../utils/api_client.dart';

class AccesorioService {
  final _api = ApiClient();
  final _endpoint = 'accesorio';

  /// 🔹 Obtener todos los registros
  Future<List<Accesorio>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Accesorio>((e) => Accesorio.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Accesorio> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Accesorio.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Accesorio item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Accesorio item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
