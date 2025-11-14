import '../utils/json_utils.dart';


/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Tarea {
  // 🔹 Atributos propios
  int? id;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)

  // 🔹 Constructor
  Tarea({
      this.id,
  })
  ;

    // 🔹 fromJson factory
    factory Tarea.fromJson(Map<String, dynamic> json) {
    return Tarea(
    id: autoConvert<int>(json['id']),
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,

    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
