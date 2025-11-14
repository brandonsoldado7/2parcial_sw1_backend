import '../utils/json_utils.dart';
import 'electrodomestico.dart';

import 'electrodomestico.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Refrigerador extends Electrodomestico {
  // 🔹 Atributos propios
  int? capacidad;
  bool? congelador;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Electrodomestico? electrodomestico;

  // 🔹 Constructor
  Refrigerador({
    int? id,
    String? marca,
    String? modelo,
    double? precio,
    String? consumo,
      this.capacidad,
      this.congelador,
    this.electrodomestico
  })
   : super(
    id: id,
    marca: marca,
    modelo: modelo,
    precio: precio,
    consumo: consumo
  );

    // 🔹 fromJson factory
    factory Refrigerador.fromJson(Map<String, dynamic> json) {
    return Refrigerador(
    id: autoConvert<int>(json['id']),
    marca: autoConvert<String>(json['marca']),
    modelo: autoConvert<String>(json['modelo']),
    precio: autoConvert<double>(json['precio']),
    consumo: autoConvert<String>(json['consumo']),
    capacidad: autoConvert<int>(json['capacidad']),
    congelador: autoConvert<bool>(json['congelador']),
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
      ...super.toJson(),

      'id': id,
      'capacidad': capacidad,
      'congelador': congelador,

      'electrodomestico': electrodomestico != null ? {'id': electrodomestico!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
