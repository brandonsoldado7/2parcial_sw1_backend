import '../utils/json_utils.dart';


/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Tipo {
  // 🔹 Atributos propios
  int? id;
  String? nombre;
  String? descripcion;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)

  // 🔹 Constructor
  Tipo({
      this.id,
      this.nombre,
      this.descripcion,
  })
  ;

    // 🔹 fromJson factory
    factory Tipo.fromJson(Map<String, dynamic> json) {
    return Tipo(
    id: autoConvert<int>(json['id']),
    nombre: autoConvert<String>(json['nombre']),
    descripcion: autoConvert<String>(json['descripcion']),
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {

      'id': id,
      'nombre': nombre,
      'descripcion': descripcion,

    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
