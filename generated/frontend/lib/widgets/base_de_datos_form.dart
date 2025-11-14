import 'package:flutter/material.dart';
import '../models/base_de_datos.dart';
import '../models/reporte.dart';
import '../services/reporte_service.dart';

class BaseDeDatosForm extends StatefulWidget {
  final void Function(BaseDeDatos) onSaved;
  final BaseDeDatos? initialData; // para editar

  const BaseDeDatosForm({super.key, required this.onSaved, this.initialData});

  @override
  State<BaseDeDatosForm> createState() => _BaseDeDatosFormState();
}

class _BaseDeDatosFormState extends State<BaseDeDatosForm> {
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
          ? 'Nuevo BaseDeDatos'
          : 'Editar BaseDeDatos'),
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

            widget.onSaved(BaseDeDatos.fromJson(_data));
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
