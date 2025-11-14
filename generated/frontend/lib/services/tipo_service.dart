import '../models/tipo.dart';
import '../utils/api_client.dart';

class TipoService {
  final _api = ApiClient();
  final _endpoint = 'tipo';

  /// 🔹 Obtener todos los registros
  Future<List<Tipo>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Tipo>((e) => Tipo.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Tipo> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Tipo.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Tipo item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Tipo item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
