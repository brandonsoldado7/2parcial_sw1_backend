import 'package:flutter/material.dart';
import '../models/lavadora.dart';
import '../models/electrodomestico.dart';
import '../services/electrodomestico_service.dart';

class LavadoraForm extends StatefulWidget {
  final void Function(Lavadora) onSaved;
  final Lavadora? initialData; // para editar

  const LavadoraForm({super.key, required this.onSaved, this.initialData});

  @override
  State<LavadoraForm> createState() => _LavadoraFormState();
}

class _LavadoraFormState extends State<LavadoraForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedElectrodomesticoId;
  List<Electrodomestico> _electrodomesticoList = [];
  final _electrodomesticoService = ElectrodomesticoService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadElectrodomesticoList();
  }

  Future<void> _loadElectrodomesticoList() async {
    final data = await _electrodomesticoService.getAll();
    setState(() => _electrodomesticoList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo Lavadora'
          : 'Editar Lavadora'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.capacidad?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'capacidad',
                        hintText: 'Ingrese un número entero',
                    ),
                    onSaved: (val) => _data['capacidad'] = val,
                      keyboardType: TextInputType.number,
                  ),
                  TextFormField(
                    initialValue: widget.initialData?.rpm?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'rpm',
                        hintText: 'Ingrese un número entero',
                    ),
                    onSaved: (val) => _data['rpm'] = val,
                      keyboardType: TextInputType.number,
                  ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedElectrodomesticoId,
                decoration: InputDecoration(labelText: 'Electrodomestico'),
                items: _electrodomesticoList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedElectrodomesticoId = val),
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
            if (_selectedElectrodomesticoId != null) {
              _data['electrodomestico'] = {'id': _selectedElectrodomesticoId};
            }

            widget.onSaved(Lavadora.fromJson(_data));
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
