import 'package:flutter/material.dart';
import '../models/${snakeCase}.dart';

class ${className}DetailScreen extends StatelessWidget {
  final ${className} item;

  const ${className}DetailScreen({super.key, required this.item});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('${className} Detalle')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

              <#-- 🔹 Mostrar atributos simples -->
              <#list entity.attributes as attr>
              Text(
                '${attr.name}: <#noparse>${item.</#noparse>${attr.name} ?? "-"}',
                style: const TextStyle(fontSize: 16),
              ),
              const SizedBox(height: 8),
              </#list>

              <#-- 🔹 Mostrar relaciones si existen -->
              <#if entity.relations?? && entity.relations?size gt 0>
                <#list entity.relations as rel>
                  <#if rel.type == "ManyToOne" || rel.type == "OneToOne">
              Text(
                '${rel.target}: <#noparse>${item.</#noparse>${rel.target?lower_case}?.toString() ?? "-"}',
                style: const TextStyle(fontSize: 16, fontStyle: FontStyle.italic),
              ),
              const SizedBox(height: 8),
                  </#if>
                </#list>
              </#if>

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
