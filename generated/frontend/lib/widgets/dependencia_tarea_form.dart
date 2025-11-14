import 'package:flutter/material.dart';
import '../models/dependencia_tarea.dart';
import '../models/tarea.dart';
import '../services/tarea_service.dart';
import '../models/tarea.dart';
import '../services/tarea_service.dart';

class DependenciaTareaForm extends StatefulWidget {
  final void Function(DependenciaTarea) onSaved;
  final DependenciaTarea? initialData; // para editar

  const DependenciaTareaForm({super.key, required this.onSaved, this.initialData});

  @override
  State<DependenciaTareaForm> createState() => _DependenciaTareaFormState();
}

class _DependenciaTareaFormState extends State<DependenciaTareaForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedTareaId;
  List<Tarea> _tareaList = [];
  final _tareaService = TareaService();
  int? _selectedTareaId;
  List<Tarea> _tareaList = [];
  final _tareaService = TareaService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadTareaList();
    _loadTareaList();
  }

  Future<void> _loadTareaList() async {
    final data = await _tareaService.getAll();
    setState(() => _tareaList = data);
  }
  Future<void> _loadTareaList() async {
    final data = await _tareaService.getAll();
    setState(() => _tareaList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo DependenciaTarea'
          : 'Editar DependenciaTarea'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedTareaId,
                decoration: InputDecoration(labelText: 'Tarea'),
                items: _tareaList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedTareaId = val),
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedTareaId,
                decoration: InputDecoration(labelText: 'Tarea'),
                items: _tareaList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedTareaId = val),
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
            if (_selectedTareaId != null) {
              _data['tarea'] = {'id': _selectedTareaId};
            }
            if (_selectedTareaId != null) {
              _data['tarea'] = {'id': _selectedTareaId};
            }

            widget.onSaved(DependenciaTarea.fromJson(_data));
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
