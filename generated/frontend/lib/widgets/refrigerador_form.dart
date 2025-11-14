import 'package:flutter/material.dart';
import '../models/refrigerador.dart';
import '../models/electrodomestico.dart';
import '../services/electrodomestico_service.dart';

class RefrigeradorForm extends StatefulWidget {
  final void Function(Refrigerador) onSaved;
  final Refrigerador? initialData; // para editar

  const RefrigeradorForm({super.key, required this.onSaved, this.initialData});

  @override
  State<RefrigeradorForm> createState() => _RefrigeradorFormState();
}

class _RefrigeradorFormState extends State<RefrigeradorForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedElectrodomesticoId;
  List<Electrodomestico> _electrodomesticoList = [];
  final _electrodomesticoService = ElectrodomesticoService();

  bool _congeladorValue = false;

  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

      _congeladorValue = json['congelador'] == true;
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
          ? 'Nuevo Refrigerador'
          : 'Editar Refrigerador'),
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
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('congelador'),
                  Switch(
                    value: _congeladorValue,
                    onChanged: (val) => setState(() => _congeladorValue = val),
                  ),
                ],
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
            _data['congelador'] = _congeladorValue;

            // 🔹 Guardar relaciones (ManyToOne)
            if (_selectedElectrodomesticoId != null) {
              _data['electrodomestico'] = {'id': _selectedElectrodomesticoId};
            }

            widget.onSaved(Refrigerador.fromJson(_data));
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
