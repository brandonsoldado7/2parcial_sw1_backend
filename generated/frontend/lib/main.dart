import 'package:flutter/material.dart';

// ==========================================================
// 📱 main.dart generado automáticamente por CodeGenFlutterService
// appName: SchoolApp
// ==========================================================

import 'screens/item_pedido_list_screen.dart';
import 'screens/pedido_list_screen.dart';
import 'screens/circulo_list_screen.dart';
import 'screens/figura_list_screen.dart';
import 'screens/base_de_datos_list_screen.dart';
import 'screens/reporte_list_screen.dart';
import 'screens/tarea_list_screen.dart';
import 'screens/dependencia_tarea_list_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'SchoolApp',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: const AppHome(),
    );
  }
}

// ==========================================================
// 🧭 Navegación automática entre entidades generadas (Drawer lateral)
// ==========================================================
class AppHome extends StatefulWidget {
  const AppHome({super.key});

  @override
  State<AppHome> createState() => _AppHomeState();
}

class _AppHomeState extends State<AppHome> {
  int _selectedIndex = 0;

  // 🔹 Lista de pantallas generadas dinámicamente
  final List<Widget> _screens = const [
    ItemPedidoListScreen(),
    PedidoListScreen(),
    CirculoListScreen(),
    FiguraListScreen(),
    BaseDeDatosListScreen(),
    ReporteListScreen(),
    TareaListScreen(),
    DependenciaTareaListScreen(),
  ];

  // 🔹 Lista de nombres de entidades (para mostrar en el Drawer)
  final List<String> _labels = const [
    'ItemPedidos',
    'Pedidos',
    'Circulos',
    'Figuras',
    'BaseDeDatoss',
    'Reportes',
    'Tareas',
    'DependenciaTareas',
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_labels[_selectedIndex]),
        backgroundColor: Colors.blue.shade700,
      ),
      drawer: Drawer(
        child: ListView.builder(
          itemCount: _labels.length,
          itemBuilder: (context, index) {
            return ListTile(
              leading: const Icon(Icons.folder_open),
              title: Text(
                _labels[index],
                style: TextStyle(
                  fontWeight: index == _selectedIndex ? FontWeight.bold : FontWeight.normal,
                  color: index == _selectedIndex ? Colors.blue : Colors.black,
                ),
              ),
              onTap: () {
                Navigator.pop(context); // Cierra el Drawer
                setState(() => _selectedIndex = index);
              },
            );
          },
        ),
      ),
      body: _screens[_selectedIndex],
    );
  }
}
