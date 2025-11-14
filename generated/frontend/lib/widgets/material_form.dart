import 'package:flutter/material.dart';
import '../models/material.dart';
import '../models/electrodomestico.dart';
import '../services/electrodomestico_service.dart';

class MaterialForm extends StatefulWidget {
  final void Function(Material) onSaved;
  final Material? initialData; // para editar

  const MaterialForm({super.key, required this.onSaved, this.initialData});

  @override
  State<MaterialForm> createState() => _MaterialFormState();
}

class _MaterialFormState extends State<MaterialForm> {
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
          ? 'Nuevo Material'
          : 'Editar Material'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.nombre?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'nombre',
                        hintText: 'Ingrese texto (String)',
                    ),
                    onSaved: (val) => _data['nombre'] = val,
                      keyboardType: TextInputType.text,
                  ),
                  TextFormField(
                    initialValue: widget.initialData?.precio?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'precio',
                        hintText: 'Ingrese un número decimal',
                    ),
                    onSaved: (val) => _data['precio'] = val,
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
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

            widget.onSaved(Material.fromJson(_data));
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
