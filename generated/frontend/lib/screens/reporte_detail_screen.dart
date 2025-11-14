import 'package:flutter/material.dart';
import '../models/reporte.dart';

class ReporteDetailScreen extends StatelessWidget {
  final Reporte item;

  const ReporteDetailScreen({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Reporte Detalle')),
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
                'BaseDeDatos: ${item.basededatos?.toString() ?? "-"}',
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
