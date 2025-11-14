import 'package:flutter/material.dart';
import '../models/electrodomestico.dart';

class ElectrodomesticoDetailScreen extends StatelessWidget {
  final Electrodomestico item;

  const ElectrodomesticoDetailScreen({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Electrodomestico Detalle')),
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
                'marca: ${item.marca ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'modelo: ${item.modelo ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'precio: ${item.precio ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'consumo: ${item.consumo ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),

              Text(
                'Tipo: ${item.tipo?.toString() ?? "-"}',
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
