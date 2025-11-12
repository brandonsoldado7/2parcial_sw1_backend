// ==========================================================
// 🔧 json_utils.dart
// Generado automáticamente por CodeGenFlutterService
// ----------------------------------------------------------
// Funciones de conversión seguras para tipos comunes de Dart
// ==========================================================

dynamic _tryParseNum(dynamic value) {
  if (value == null) return null;
  if (value is num) return value;
  if (value is String) {
    final n = num.tryParse(value);
    return n;
  }
  return null;
}

/// Convierte de forma segura a tipo genérico <T>
/// - Soporta int, double, bool, DateTime, String
/// - Si no puede convertir, retorna null
T? autoConvert<T>(dynamic value) {
  if (value == null) return null;

  if (T == int) {
    final n = _tryParseNum(value);
    return (n != null) ? n.toInt() as T : null;
  }
  if (T == double) {
    final n = _tryParseNum(value);
    return (n != null) ? n.toDouble() as T : null;
  }
  if (T == bool) {
    if (value is bool) return value as T;
    if (value is String) {
      final lower = value.toLowerCase();
      if (['true', '1', 'yes', 'y', 'si'].contains(lower)) return true as T;
      if (['false', '0', 'no', 'n'].contains(lower)) return false as T;
    }
    if (value is num) return (value != 0) as T;
    return null;
  }
  if (T == DateTime) {
    if (value is DateTime) return value as T;
    if (value is String) {
      try {
        return DateTime.parse(value) as T;
      } catch (_) {
        return null;
      }
    }
    return null;
  }
  if (T == String) {
    return value.toString() as T;
  }

  // Cualquier otro tipo, devuelve tal cual
  return value as T?;
}

/// Conversión segura a int
int? toInt(dynamic value) => autoConvert<int>(value);

/// Conversión segura a double
double? toDouble(dynamic value) => autoConvert<double>(value);

/// Conversión segura a bool
bool? toBool(dynamic value) => autoConvert<bool>(value);

/// Conversión segura a DateTime
DateTime? toDateTime(dynamic value) => autoConvert<DateTime>(value);

/// Conversión segura a String
String? toStringSafe(dynamic value) => autoConvert<String>(value);
