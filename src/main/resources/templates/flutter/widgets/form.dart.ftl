import 'package:flutter/material.dart';
import '../models/${snakeCase}.dart';
<#if entity.relations??>
  <#list entity.relations as rel>
<#assign relSnake = camelToSnake(rel.target)>
import '../models/${relSnake}.dart';
import '../services/${relSnake}_service.dart';
  </#list>
</#if>

class ${className}Form extends StatefulWidget {
  final void Function(${className}) onSaved;
  final ${className}? initialData; // para editar

  const ${className}Form({super.key, required this.onSaved, this.initialData});

  @override
  State<${className}Form> createState() => _${className}FormState();
}

class _${className}FormState extends State<${className}Form> {
  final _formKey = GlobalKey<FormState>();
  final Map<String, dynamic> _data = {};

  <#-- 🔹 Variables para relaciones ManyToOne -->
  <#if entity.relations??>
    <#list entity.relations as rel>
      <#if rel.type == "ManyToOne">
  int? _selected${rel.target}Id;
  List<${rel.target}> _${rel.target?lower_case}List = [];
  final _${rel.target?lower_case}Service = ${rel.target}Service();
      </#if>
    </#list>
  </#if>

  <#-- 🔹 Variables para booleanos -->
  <#list entity.attributes as attr>
    <#if attr.type?lower_case == "boolean" || attr.type?lower_case == "bool">
  bool _${attr.name}Value = false;
    </#if>
  </#list>

  @override
  void initState() {
    super.initState();

    // 🔹 Precargar datos si es edición
    if (widget.initialData != null) {
      final json = widget.initialData!.toJson();
      _data.addAll(json);

      <#list entity.attributes as attr>
        <#if attr.type?lower_case == "boolean" || attr.type?lower_case == "bool">
      _${attr.name}Value = json['${attr.name}'] == true;
        </#if>
      </#list>
    }

    // 🔹 Cargar listas de relaciones ManyToOne
    <#if entity.relations??>
      <#list entity.relations as rel>
        <#if rel.type == "ManyToOne">
    _load${rel.target}List();
        </#if>
      </#list>
    </#if>
  }

  <#-- 🔹 Métodos para cargar relaciones -->
  <#if entity.relations??>
    <#list entity.relations as rel>
      <#if rel.type == "ManyToOne">
  Future<void> _load${rel.target}List() async {
    final data = await _${rel.target?lower_case}Service.getAll();
    setState(() => _${rel.target?lower_case}List = data);
  }
      </#if>
    </#list>
  </#if>

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.initialData == null
          ? 'Nuevo ${className}'
          : 'Editar ${className}'),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [

              <#-- 🔹 Campos normales (sin ID) -->
              <#list entity.attributes as attr>
                <#if !attr.isId?? || !attr.isId>
                  <#if attr.type?lower_case == "boolean" || attr.type?lower_case == "bool">
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('${attr.name}'),
                  Switch(
                    value: _${attr.name}Value,
                    onChanged: (val) => setState(() => _${attr.name}Value = val),
                  ),
                ],
              ),

                  <#else>
                  TextFormField(
                    initialValue: widget.initialData?.${attr.name}?.toString() ?? '',
                    decoration: InputDecoration(
                      labelText: '${attr.name}',
                      <#-- 🔹 Mostrar sugerencia según tipo -->
                      <#if attr.type?lower_case == "string">
                        hintText: 'Ingrese texto (String)',
                      <#elseif attr.type?lower_case == "int" || attr.type?lower_case == "long">
                        hintText: 'Ingrese un número entero',
                      <#elseif attr.type?lower_case == "double" || attr.type?lower_case == "float">
                        hintText: 'Ingrese un número decimal',
                      <#elseif attr.type?lower_case == "date" || attr.type?lower_case == "localdatetime" || attr.type?contains("java.util.date")>
                        hintText: 'Formato: yyyy-MM-dd',
                      <#else>
                        hintText: 'Ingrese valor',
                      </#if>
                    ),
                    onSaved: (val) => _data['${attr.name}'] = val,
                    <#-- 🔹 Teclado según tipo -->
                    <#if attr.type?lower_case == "int" || attr.type?lower_case == "long">
                      keyboardType: TextInputType.number,
                    <#elseif attr.type?lower_case == "double" || attr.type?lower_case == "float">
                      keyboardType: const TextInputType.numberWithOptions(decimal: true),
                    <#else>
                      keyboardType: TextInputType.text,
                    </#if>
                  ),
                  </#if>
                </#if>
              </#list>
              <#-- 🔹 Campos de relación ManyToOne -->
              <#if entity.relations?? && entity.relations?size gt 0>
                <#list entity.relations as rel>
                  <#if rel.type == "ManyToOne">
              const SizedBox(height: 10),
              DropdownButtonFormField<int>(
                value: _selected${rel.target}Id,
                decoration: InputDecoration(labelText: '${rel.target}'),
                items: _${rel.target?lower_case}List.map((e) {
                  return DropdownMenuItem<int>(
                    value: e.id,
                    child: Text(e.toString()),
                  );
                }).toList(),
                onChanged: (val) => setState(() => _selected${rel.target}Id = val),
              ),
                  </#if>
                </#list>
              </#if>
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () {
            _formKey.currentState?.save();

            // 🔹 Guardar booleanos
            <#list entity.attributes as attr>
              <#if attr.type?lower_case == "boolean" || attr.type?lower_case == "bool">
            _data['${attr.name}'] = _${attr.name}Value;
              </#if>
            </#list>

            // 🔹 Guardar relaciones (ManyToOne)
            <#if entity.relations??>
              <#list entity.relations as rel>
                <#if rel.type == "ManyToOne">
            if (_selected${rel.target}Id != null) {
              _data['${rel.target?lower_case}'] = {'id': _selected${rel.target}Id};
            }
                </#if>
              </#list>
            </#if>

            widget.onSaved(${className}.fromJson(_data));
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
