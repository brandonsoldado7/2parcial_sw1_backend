import '../utils/json_utils.dart';

import 'tarea.dart';
import 'tarea.dart';

/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class DependenciaTarea {
  // 🔹 Atributos propios
  int? id;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)
  Tarea? tarea;
  Tarea? tarea;

  // 🔹 Constructor
  DependenciaTarea({
      this.id,
    this.tarea,
    this.tarea
  })
  ;

    // 🔹 fromJson factory
    factory DependenciaTarea.fromJson(Map<String, dynamic> json) {
    return DependenciaTarea(
    id: autoConvert<int>(json['id']),
    tarea:
      (json['tarea'] is Map<String, dynamic>)
          ? Tarea.fromJson(json['tarea'])
          : (json['tarea'] != null
              ? Tarea(id: autoConvert<int>(json['tarea']))
              : null),
    tarea:
      (json['tarea'] is Map<String, dynamic>)
          ? Tarea.fromJson(json['tarea'])
          : (json['tarea'] != null
              ? Tarea(id: autoConvert<int>(json['tarea']))
              : null)
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,

      'tarea': tarea != null ? {'id': tarea!.id} : null,
      'tarea': tarea != null ? {'id': tarea!.id} : null,
    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
