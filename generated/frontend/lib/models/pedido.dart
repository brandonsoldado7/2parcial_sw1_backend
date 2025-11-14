import '../utils/json_utils.dart';


/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Pedido {
  // 🔹 Atributos propios
  int? id;
  DateTime? fecha;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)

  // 🔹 Constructor
  Pedido({
      this.id,
      this.fecha,
  })
  ;

    // 🔹 fromJson factory
    factory Pedido.fromJson(Map<String, dynamic> json) {
    return Pedido(
    id: autoConvert<int>(json['id']),
    fecha: autoConvert<DateTime>(json['fecha']),
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,
      'fecha': fecha?.toIso8601String().split('T').first,

    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
