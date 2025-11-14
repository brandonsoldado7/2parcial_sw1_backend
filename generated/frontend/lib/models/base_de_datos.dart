import '../utils/json_utils.dart';

import 'reporte.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class BaseDeDatos {
  // 🔹 Atributos propios
  int? id;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Reporte? reporte;

  // 🔹 Constructor
  BaseDeDatos({
      this.id,
    this.reporte
  })
  ;

    // 🔹 fromJson factory
    factory BaseDeDatos.fromJson(Map<String, dynamic> json) {
    return BaseDeDatos(
    id: autoConvert<int>(json['id']),
    reporte:
      (json['reporte'] is Map<String, dynamic>)
          ? Reporte.fromJson(json['reporte'])
          : (json['reporte'] != null
              ? Reporte(id: autoConvert<int>(json['reporte']))
              : null)
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,

      'reporte': reporte != null ? {'id': reporte!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
