import '../utils/json_utils.dart';
import 'figura.dart';


/// ==========================================================
/// 📄 Modelo generado automáticamente por CodeGenFlutterService
/// ==========================================================
class Circulo extends Figura {
  // 🔹 Atributos propios
  double? radio;

  // 🔹 Relaciones (solo ManyToOne / OneToOne)

  // 🔹 Constructor
  Circulo({
    int? id,
    double? area,
      this.radio
  })
   : super(
    id: id,
    area: area
  );

    // 🔹 fromJson factory
    factory Circulo.fromJson(Map<String, dynamic> json) {
    return Circulo(
    id: autoConvert<int>(json['id']),
    area: autoConvert<double>(json['area']),
    radio: autoConvert<double>(json['radio'])
    );
    }

    // 🔹 toJson
    @override
    Map<String, dynamic> toJson() => {
      ...super.toJson(),

      'id': id,
      'radio': radio

    };


    // 🔹 toString (para vistas y Dropdowns)
    @override
    String toString() {
        return '${id ?? "s/d"}';
    }


}
