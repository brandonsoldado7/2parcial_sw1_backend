import 'package:flutter/material.dart';
import '../models/reporte.dart';
import '../models/base_de_datos.dart';
import '../services/base_de_datos_service.dart';
import '../models/figura.dart';
import '../services/figura_service.dart';

class ReporteForm extends StatefulWidget {
  final void Function(Reporte) onSaved;
  final Reporte? initialData; // para editar

  const ReporteForm({super.key, required this.onSaved, this.initialData});

  @override
  State<ReporteForm> createState() => _ReporteFormState();
}

class _ReporteFormState extends State<ReporteForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedBaseDeDatosId;
  List<BaseDeDatos> _basededatosList = [];
  final _basededatosService = BaseDeDatosService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadBaseDeDatosList();
  }

  Future<void> _loadBaseDeDatosList() async {
    final data = await _basededatosService.getAll();
    setState(() => _basededatosList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo Reporte'
          : 'Editar Reporte'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedBaseDeDatosId,
                decoration: InputDecoration(labelText: 'BaseDeDatos'),
                items: _basededatosList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedBaseDeDatosId = val),
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () {
            _formKey.currentState?.save();

            // 🔹 Guardar booleanos

            // 🔹 Guardar relaciones (ManyToOne)
            if (_selectedBaseDeDatosId != null) {
              _data['basededatos'] = {'id': _selectedBaseDeDatosId};
            }

            widget.onSaved(Reporte.fromJson(_data));
            Navigator.pop(context);
          },
          child: const Text('Guardar'),
        ),
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Cancelar'),
        ),
      ],
    );
  }
}
