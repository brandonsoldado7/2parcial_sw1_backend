import 'package:flutter/material.dart';
import '../models/figura.dart';
import '../models/circulo.dart';
import '../services/circulo_service.dart';
import '../models/reporte.dart';
import '../services/reporte_service.dart';

class FiguraForm extends StatefulWidget {
  final void Function(Figura) onSaved;
  final Figura? initialData; // para editar

  const FiguraForm({super.key, required this.onSaved, this.initialData});

  @override
  State<FiguraForm> createState() => _FiguraFormState();
}

class _FiguraFormState extends State<FiguraForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedReporteId;
  List<Reporte> _reporteList = [];
  final _reporteService = ReporteService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadReporteList();
  }

  Future<void> _loadReporteList() async {
    final data = await _reporteService.getAll();
    setState(() => _reporteList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo Figura'
          : 'Editar Figura'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.area?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'area',
                        hintText: 'Ingrese un número decimal',
                    ),
                    onSaved: (val) => _data['area'] = val,
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                  ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedReporteId,
                decoration: InputDecoration(labelText: 'Reporte'),
                items: _reporteList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedReporteId = val),
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
            if (_selectedReporteId != null) {
              _data['reporte'] = {'id': _selectedReporteId};
            }

            widget.onSaved(Figura.fromJson(_data));
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
