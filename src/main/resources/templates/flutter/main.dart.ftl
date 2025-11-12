import 'package:flutter/material.dart';

// ==========================================================
// 📱 main.dart generado automáticamente por CodeGenFlutterService
// appName: ${appName}
// ==========================================================

<#-- 🔹 Importar todas las pantallas generadas -->
<#list entities?filter(e -> e.generateCRUD?? && e.generateCRUD) as e>
<#assign relSnake2 = camelToSnake(e.name)>
import 'screens/${relSnake2}_list_screen.dart';
</#list>

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: '${appName}',
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
    <#list entities?filter(e -> e.generateCRUD?? && e.generateCRUD) as e>
    ${e.name}ListScreen(),
    </#list>
  ];

  // 🔹 Lista de nombres de entidades (para mostrar en el Drawer)
  final List<String> _labels = const [
    <#list entities?filter(e -> e.generateCRUD?? && e.generateCRUD) as e>
    '${e.name}s',
    </#list>
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
