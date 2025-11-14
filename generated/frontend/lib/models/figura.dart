import '../utils/json_utils.dart';

import 'reporte.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Figura {
  // 🔹 Atributos propios
  int? id;
  double? area;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Reporte? reporte;

  // 🔹 Constructor
  Figura({
      this.id,
      this.area,
    this.reporte
  })
  ;

    // 🔹 fromJson factory
    factory Figura.fromJson(Map<String, dynamic> json) {
    return Figura(
    id: autoConvert<int>(json['id']),
    area: autoConvert<double>(json['area']),
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
      'area': area,

      'reporte': reporte != null ? {'id': reporte!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
