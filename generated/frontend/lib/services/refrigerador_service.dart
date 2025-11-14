import '../models/refrigerador.dart';
import '../utils/api_client.dart';

class RefrigeradorService {
  final _api = ApiClient();
  final _endpoint = 'refrigerador';

  /// 🔹 Obtener todos los registros
  Future<List<Refrigerador>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Refrigerador>((e) => Refrigerador.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Refrigerador> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Refrigerador.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Refrigerador item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Refrigerador item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
