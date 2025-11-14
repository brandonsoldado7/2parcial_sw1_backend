import '../utils/json_utils.dart';

import 'electrodomestico.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Material {
  // 🔹 Atributos propios
  int? id;
  String? nombre;
  double? precio;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Electrodomestico? electrodomestico;

  // 🔹 Constructor
  Material({
      this.id,
      this.nombre,
      this.precio,
    this.electrodomestico
  })
  ;

    // 🔹 fromJson factory
    factory Material.fromJson(Map<String, dynamic> json) {
    return Material(
    id: autoConvert<int>(json['id']),
    nombre: autoConvert<String>(json['nombre']),
    precio: autoConvert<double>(json['precio']),
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
      'precio': precio,

      'electrodomestico': electrodomestico != null ? {'id': electrodomestico!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
