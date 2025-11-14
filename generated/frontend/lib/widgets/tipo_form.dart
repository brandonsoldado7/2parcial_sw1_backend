import 'package:flutter/material.dart';
import '../models/tipo.dart';
import '../models/electrodomestico.dart';
import '../services/electrodomestico_service.dart';

class TipoForm extends StatefulWidget {
  final void Function(Tipo) onSaved;
  final Tipo? initialData; // para editar

  const TipoForm({super.key, required this.onSaved, this.initialData});

  @override
  State<TipoForm> createState() => _TipoFormState();
}

class _TipoFormState extends State<TipoForm> {
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
          ? 'Nuevo Tipo'
          : 'Editar Tipo'),
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
                    initialValue: widget.initialData?.descripcion?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'descripcion',
                        hintText: 'Ingrese texto (String)',
                    ),
                    onSaved: (val) => _data['descripcion'] = val,
                      keyboardType: TextInputType.text,
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

            widget.onSaved(Tipo.fromJson(_data));
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
