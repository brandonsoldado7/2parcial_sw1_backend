import '../utils/json_utils.dart';

import 'base_de_datos.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Reporte {
  // 🔹 Atributos propios
  int? id;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  BaseDeDatos? basededatos;

  // 🔹 Constructor
  Reporte({
      this.id,
    this.basededatos,
  })
  ;

    // 🔹 fromJson factory
    factory Reporte.fromJson(Map<String, dynamic> json) {
    return Reporte(
    id: autoConvert<int>(json['id']),
    basededatos:
      (json['basededatos'] is Map<String, dynamic>)
          ? BaseDeDatos.fromJson(json['basededatos'])
          : (json['basededatos'] != null
              ? BaseDeDatos(id: autoConvert<int>(json['basededatos']))
              : null),
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,

      'basededatos': basededatos != null ? {'id': basededatos!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
