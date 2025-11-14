import '../models/circulo.dart';
import '../utils/api_client.dart';

class CirculoService {
  final _api = ApiClient();
  final _endpoint = 'circulo';

  /// 🔹 Obtener todos los registros
  Future<List<Circulo>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Circulo>((e) => Circulo.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Circulo> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Circulo.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Circulo item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Circulo item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
