import 'package:flutter/material.dart';
import '../models/accesorio.dart';

class AccesorioDetailScreen extends StatelessWidget {
  final Accesorio item;

  const AccesorioDetailScreen({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Accesorio Detalle')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

              Text(
                'id: ${item.id ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'nombre: ${item.nombre ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'fecha: ${item.fecha ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),

              Text(
                'Electrodomestico: ${item.electrodomestico?.toString() ?? "-"}',
                style: const TextStyle(fontSize: 16, fontStyle: FontStyle.italic),
              ),
              const SizedBox(height: 8),

              const SizedBox(height: 24),
              Center(
                child: ElevatedButton.icon(
                  icon: const Icon(Icons.arrow_back),
                  label: const Text('Volver'),
                  onPressed: () => Navigator.pop(context),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
