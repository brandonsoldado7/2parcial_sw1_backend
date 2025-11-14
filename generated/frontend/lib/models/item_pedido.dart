import '../utils/json_utils.dart';

import 'circulo.dart';
import 'pedido.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class ItemPedido {
  // 🔹 Atributos propios
  int? id;
  int? cantidad;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Circulo? circulo;
  Pedido? pedido;

  // 🔹 Constructor
  ItemPedido({
      this.id,
      this.cantidad,
    this.circulo,
    this.pedido
  })
  ;

    // 🔹 fromJson factory
    factory ItemPedido.fromJson(Map<String, dynamic> json) {
    return ItemPedido(
    id: autoConvert<int>(json['id']),
    cantidad: autoConvert<int>(json['cantidad']),
    circulo:
      (json['circulo'] is Map<String, dynamic>)
          ? Circulo.fromJson(json['circulo'])
          : (json['circulo'] != null
              ? Circulo(id: autoConvert<int>(json['circulo']))
              : null),
    pedido:
      (json['pedido'] is Map<String, dynamic>)
          ? Pedido.fromJson(json['pedido'])
          : (json['pedido'] != null
              ? Pedido(id: autoConvert<int>(json['pedido']))
              : null)
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,
      'cantidad': cantidad,

      'circulo': circulo != null ? {'id': circulo!.id} : null,
      'pedido': pedido != null ? {'id': pedido!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
