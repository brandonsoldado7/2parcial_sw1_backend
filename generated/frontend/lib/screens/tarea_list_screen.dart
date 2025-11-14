import 'package:flutter/material.dart';
import '../models/tarea.dart';
import '../services/tarea_service.dart';
import '../widgets/tarea_form.dart';
import 'tarea_detail_screen.dart';

class TareaListScreen extends StatefulWidget {
  const TareaListScreen({super.key});

  @override
  State<TareaListScreen> createState() => _TareaListScreenState();
}

class _TareaListScreenState extends State<TareaListScreen> {
  final _service = TareaService();
  List<Tarea> _items = [];

  @override
  void initState() {
    super.initState();
    _loadItems();
  }

  Future<void> _loadItems() async {
    try {
      final data = await _service.getAll();
      setState(() => _items = data);
    } catch (e) {
      debugPrint("Error cargando Tareas: $e");
    }
  }

  Future<void> _editar(Tarea item) async {
    await showDialog(
      context: context,
      builder: (_) => TareaForm(
        initialData: item,
        onSaved: (updated) async {
          await _service.update(item.id!, updated);
          _loadItems();
        },
      ),
    );
  }

  Future<void> _eliminar(Tarea item) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Eliminar registro'),
        content: const Text('¿Seguro que deseas eliminar este registro?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('Eliminar')),
        ],
      ),
    );

    if (confirm == true) {
      await _service.delete(item.id!);
      _loadItems();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Tareas')),
      body: _items.isEmpty
          ? const Center(child: Text('No hay datos disponibles'))
          : ListView.builder(
              itemCount: _items.length,
              itemBuilder: (context, index) {
                final item = _items[index];

                return Card(
                  margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                  child: ListTile(
                    title: Text(
                      'ID: ${item.toString()}',
                      style: const TextStyle(fontWeight: FontWeight.bold),
                    ),
                    subtitle: Text(
                      item.toJson().toString(),
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.edit, color: Colors.blueAccent),
                          onPressed: () => _editar(item),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete, color: Colors.redAccent),
                          onPressed: () => _eliminar(item),
                        ),
                      ],
                    ),
                    onTap: () async {
                      final changed = await Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => TareaDetailScreen(item: item),
                        ),
                      );
                      if (changed == true) {
                        _loadItems();
                      }
                    },
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => showDialog(
          context: context,
          builder: (_) => TareaForm(onSaved: (newItem) async {
            await _service.create(newItem);
            _loadItems();
          }),
        ),
        child: const Icon(Icons.add),
      ),
    );
  }
}
