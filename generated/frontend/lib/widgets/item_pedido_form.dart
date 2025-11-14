import 'package:flutter/material.dart';
import '../models/item_pedido.dart';
import '../models/circulo.dart';
import '../services/circulo_service.dart';
import '../models/pedido.dart';
import '../services/pedido_service.dart';

class ItemPedidoForm extends StatefulWidget {
  final void Function(ItemPedido) onSaved;
  final ItemPedido? initialData; // para editar

  const ItemPedidoForm({super.key, required this.onSaved, this.initialData});

  @override
  State<ItemPedidoForm> createState() => _ItemPedidoFormState();
}

class _ItemPedidoFormState extends State<ItemPedidoForm> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  int? _selectedCirculoId;
  List<Circulo> _circuloList = [];
  final _circuloService = CirculoService();
  int? _selectedPedidoId;
  List<Pedido> _pedidoList = [];
  final _pedidoService = PedidoService();


  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

    }

    // 🔹 Cargar listas de relaciones ManyToOne
    _loadCirculoList();
    _loadPedidoList();
  }

  Future<void> _loadCirculoList() async {
    final data = await _circuloService.getAll();
    setState(() => _circuloList = data);
  }
  Future<void> _loadPedidoList() async {
    final data = await _pedidoService.getAll();
    setState(() => _pedidoList = data);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo ItemPedido'
          : 'Editar ItemPedido'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

                  TextFormField(
                    initialValue: widget.initialData?.cantidad?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: 'cantidad',
                        hintText: 'Ingrese un número entero',
                    ),
                    onSaved: (val) => _data['cantidad'] = val,
                      keyboardType: TextInputType.number,
                  ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedCirculoId,
                decoration: InputDecoration(labelText: 'Circulo'),
                items: _circuloList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedCirculoId = val),
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selectedPedidoId,
                decoration: InputDecoration(labelText: 'Pedido'),
                items: _pedidoList.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selectedPedidoId = val),
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
            if (_selectedCirculoId != null) {
              _data['circulo'] = {'id': _selectedCirculoId};
            }
            if (_selectedPedidoId != null) {
              _data['pedido'] = {'id': _selectedPedidoId};
            }

            widget.onSaved(ItemPedido.fromJson(_data));
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
