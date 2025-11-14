import 'package:flutter/material.dart';
import '../models/circulo.dart';

class CirculoForm extends StatefulWidget {
  final void Function(Circulo) onSaved;
  final Circulo? initialData; // para editar

  const CirculoForm({super.key, required this.onSaved, this.initialData});

  @override
  State<CirculoForm> createState() => _CirculoFormState();
}

class _CirculoFormState extends State<CirculoForm> {
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
          ? 'Nuevo Circulo'
          : 'Editar Circulo'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.radio?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'radio',
                        hintText: 'Ingrese un número decimal',
                    ),
                    onSaved: (val) => _data['radio'] = val,
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
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

            widget.onSaved(Circulo.fromJson(_data));
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
