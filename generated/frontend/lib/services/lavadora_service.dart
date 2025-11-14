import '../models/lavadora.dart';
import '../utils/api_client.dart';

class LavadoraService {
  final _api = ApiClient();
  final _endpoint = 'lavadora';

  /// 🔹 Obtener todos los registros
  Future<List<Lavadora>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Lavadora>((e) => Lavadora.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Lavadora> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Lavadora.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Lavadora item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Lavadora item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
