import 'package:flutter/material.dart';
import '../models/tarea.dart';
import '../models/dependencia_tarea.dart';
import '../services/dependencia_tarea_service.dart';
import '../models/dependencia_tarea.dart';
import '../services/dependencia_tarea_service.dart';

class TareaForm extends StatefulWidget {
  final void Function(Tarea) onSaved;
  final Tarea? initialData; // para editar

  const TareaForm({super.key, required this.onSaved, this.initialData});

  @override
  State<TareaForm> createState() => _TareaFormState();
}

class _TareaFormState extends State<TareaForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};



  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
  }


  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo Tarea'
          : 'Editar Tarea'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

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

            widget.onSaved(Tarea.fromJson(_data));
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
