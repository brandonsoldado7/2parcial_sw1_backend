import '../models/base_de_datos.dart';
import '../utils/api_client.dart';

class BaseDeDatosService {
  final _api = ApiClient();
  final _endpoint = 'basededatos';

  /// 🔹 Obtener todos los registros
  Future<List<BaseDeDatos>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<BaseDeDatos>((e) => BaseDeDatos.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<BaseDeDatos> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return BaseDeDatos.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(BaseDeDatos item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, BaseDeDatos item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
