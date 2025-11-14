import '../utils/json_utils.dart';

import 'electrodomestico.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Accesorio {
  // 🔹 Atributos propios
  int? id;
  String? nombre;
  DateTime? fecha;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Electrodomestico? electrodomestico;

  // 🔹 Constructor
  Accesorio({
      this.id,
      this.nombre,
      this.fecha,
    this.electrodomestico
  })
  ;

    // 🔹 fromJson factory
    factory Accesorio.fromJson(Map<String, dynamic> json) {
    return Accesorio(
    id: autoConvert<int>(json['id']),
    nombre: autoConvert<String>(json['nombre']),
    fecha: autoConvert<DateTime>(json['fecha']),
    electrodomestico:
      (json['electrodomestico'] is Map<String, dynamic>)
          ? Electrodomestico.fromJson(json['electrodomestico'])
          : (json['electrodomestico'] != null
              ? Electrodomestico(id: autoConvert<int>(json['electrodomestico']))
              : null)
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,
      'nombre': nombre,
      'fecha': fecha?.toIso8601String().split('T').first,

      'electrodomestico': electrodomestico != null ? {'id': electrodomestico!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
