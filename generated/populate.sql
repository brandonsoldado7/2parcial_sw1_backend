INSERT INTO base_de_datos (id) VALUES
(1),
(2),
(3),
(4);

INSERT INTO reporte (id, base_de_datos_id) VALUES
(1, 1),
(2, 2),
(3, NULL),
(4, 3);

INSERT INTO figura (id, area, reporte_id) VALUES
(1, 28.27, 1),
(2, 50.24, 1),
(3, 12.57, 2),
(4, 78.5, 4);

INSERT INTO circulo (id, radio) VALUES
(1, 3.0),
(2, 4.0),
(3, 2.0),
(4, 5.0);

INSERT INTO pedido (id, fecha) VALUES
(1, '2021-03-15'),
(2, '2020-11-20'),
(3, '2022-06-30'),
(4, '2023-01-10');

INSERT INTO item_pedido (id, cantidad, circulo_id, pedido_id) VALUES
(1, 2, 1, 1),
(2, 5, 2, 1),
(3, 1, 3, 2),
(4, 3, 4, 4);

INSERT INTO tarea (id) VALUES
(1),
(2),
(3),
(4);

INSERT INTO dependencia_tarea (id, tarea_id, tarea_id_2) VALUES
(1, 1, 2),
(2, 2, 3),
(3, 3, 4),
(4, 4, 1);