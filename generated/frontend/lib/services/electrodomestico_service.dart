import '../models/electrodomestico.dart';
import '../utils/api_client.dart';

class ElectrodomesticoService {
  final _api = ApiClient();
  final _endpoint = 'electrodomestico';

  /// 🔹 Obtener todos los registros
  Future<List<Electrodomestico>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Electrodomestico>((e) => Electrodomestico.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Electrodomestico> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Electrodomestico.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Electrodomestico item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Electrodomestico item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
