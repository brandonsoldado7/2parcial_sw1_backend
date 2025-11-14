import 'package:flutter/material.dart';
import '../models/refrigerador.dart';

class RefrigeradorDetailScreen extends StatelessWidget {
  final Refrigerador item;

  const RefrigeradorDetailScreen({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Refrigerador Detalle')),
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
                'capacidad: ${item.capacidad ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              Text(
                'congelador: ${item.congelador ?? "-"}',
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
