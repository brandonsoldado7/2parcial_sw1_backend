import '../utils/json_utils.dart';

import 'tipo.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Electrodomestico {
  // 🔹 Atributos propios
  int? id;
  String? marca;
  String? modelo;
  double? precio;
  String? consumo;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Tipo? tipo;

  // 🔹 Constructor
  Electrodomestico({
      this.id,
      this.marca,
      this.modelo,
      this.precio,
      this.consumo,
    this.tipo
  })
  ;

    // 🔹 fromJson factory
    factory Electrodomestico.fromJson(Map<String, dynamic> json) {
    return Electrodomestico(
    id: autoConvert<int>(json['id']),
    marca: autoConvert<String>(json['marca']),
    modelo: autoConvert<String>(json['modelo']),
    precio: autoConvert<double>(json['precio']),
    consumo: autoConvert<String>(json['consumo']),
    tipo:
      (json['tipo'] is Map<String, dynamic>)
          ? Tipo.fromJson(json['tipo'])
          : (json['tipo'] != null
              ? Tipo(id: autoConvert<int>(json['tipo']))
              : null)
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,
      'marca': marca,
      'modelo': modelo,
      'precio': precio,
      'consumo': consumo,

      'tipo': tipo != null ? {'id': tipo!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
