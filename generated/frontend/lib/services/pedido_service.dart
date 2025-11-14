import '../models/pedido.dart';
import '../utils/api_client.dart';

class PedidoService {
  final _api = ApiClient();
  final _endpoint = 'pedido';

  /// 🔹 Obtener todos los registros
  Future<List<Pedido>> getAll() async {
    final data = await _api.getList(_endpoint);
    return data.map<Pedido>((e) => Pedido.fromJson(e)).toList();
  }

  /// 🔹 Obtener registro por ID
  Future<Pedido> getById(int id) async {
    final data = await _api.getById(_endpoint, id);
    return Pedido.fromJson(data);
  }

  /// 🔹 Crear nuevo registro
  Future<void> create(Pedido item) async {
    await _api.create(_endpoint, item.toJson());
  }

  /// 🔹 Actualizar registro existente
  Future<void> update(int id, Pedido item) async {
    await _api.update(_endpoint, id, item.toJson());
  }

  /// 🔹 Eliminar registro
  Future<void> delete(int id) async {
    await _api.delete(_endpoint, id);
  }
}
