import 'package:flutter/material.dart';
import '../models/electrodomestico.dart';
import '../models/lavadora.dart';
import '../services/lavadora_service.dart';
import '../models/refrigerador.dart';
import '../services/refrigerador_service.dart';
import '../models/material.dart';
import '../services/material_service.dart';
import '../models/accesorio.dart';
import '../services/accesorio_service.dart';
import '../models/tipo.dart';
import '../services/tipo_service.dart';

class ElectrodomesticoForm extends StatefulWidget {
  final void Function(Electrodomestico) onSaved;
  final Electrodomestico? initialData; // para editar

  const ElectrodomesticoForm({super.key, required this.onSaved, this.initialData});

  @override
  State<ElectrodomesticoForm> createState() => _ElectrodomesticoFormState();
}

class _ElectrodomesticoFormState extends State<ElectrodomesticoForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedTipoId;
  List<Tipo> _tipoList = [];
  final _tipoService = TipoService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadTipoList();
  }

  Future<void> _loadTipoList() async {
    final data = await _tipoService.getAll();
    setState(() => _tipoList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo Electrodomestico'
          : 'Editar Electrodomestico'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.marca?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'marca',
                        hintText: 'Ingrese texto (String)',
                    ),
                    onSaved: (val) => _data['marca'] = val,
                      keyboardType: TextInputType.text,
                  ),
                  TextFormField(
                    initialValue: widget.initialData?.modelo?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'modelo',
                        hintText: 'Ingrese texto (String)',
                    ),
                    onSaved: (val) => _data['modelo'] = val,
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
                  TextFormField(
                    initialValue: widget.initialData?.consumo?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'consumo',
                        hintText: 'Ingrese texto (String)',
                    ),
                    onSaved: (val) => _data['consumo'] = val,
                      keyboardType: TextInputType.text,
                  ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedTipoId,
                decoration: InputDecoration(labelText: 'Tipo'),
                items: _tipoList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedTipoId = val),
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
            if (_selectedTipoId != null) {
              _data['tipo'] = {'id': _selectedTipoId};
            }

            widget.onSaved(Electrodomestico.fromJson(_data));
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
