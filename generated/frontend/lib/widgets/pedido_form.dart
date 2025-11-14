import 'package:flutter/material.dart';
import '../models/pedido.dart';
import '../models/item_pedido.dart';
import '../services/item_pedido_service.dart';

class PedidoForm extends StatefulWidget {
  final void Function(Pedido) onSaved;
  final Pedido? initialData; // para editar

  const PedidoForm({super.key, required this.onSaved, this.initialData});

  @override
  State<PedidoForm> createState() => _PedidoFormState();
}

class _PedidoFormState extends State<PedidoForm> {
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
          ? 'Nuevo Pedido'
          : 'Editar Pedido'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.fecha?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'fecha',
                        hintText: 'Formato: yyyy-MM-dd',
                    ),
                    onSaved: (val) => _data['fecha'] = val,
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

            widget.onSaved(Pedido.fromJson(_data));
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
