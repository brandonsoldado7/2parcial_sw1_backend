INSERT INTO tipo (id, nombre, descripcion) VALUES
(1, 'Lavadora', 'Electrodomésticos para lavar ropa'),
(2, 'Refrigerador', 'Electrodomésticos para conservar alimentos'),
(3, 'Microondas', 'Electrodomésticos para calentar alimentos'),
(4, 'Aspiradora', 'Electrodomésticos para limpieza');

INSERT INTO electrodomestico (id, marca, modelo, precio, consumo, tipo_id) VALUES
(1, 'LG', 'X123', 450.50, 'A++', 1),
(2, 'Samsung', 'Cooler200', 780.00, 'A+', 2),
(3, 'Whirlpool', 'FastWash', 350.99, 'B', 1),
(4, 'Bosch', 'FreezeMax', 900.45, 'A++', 2);

INSERT INTO material (id, nombre, precio) VALUES
(1, 'Plastic', 10.00),
(2, 'Steel', 25.50),
(3, 'Rubber', 7.75),
(4, 'Glass', 15.30);

INSERT INTO accesorio (id, nombre, fecha) VALUES
(1, 'Filtro de agua', '2020-05-20'),
(2, 'Manguera extra', '2021-11-15'),
(3, 'Recipiente para ropa', '2019-09-30'),
(4, 'Cuchilla extra', '2022-03-10');

INSERT INTO lavadora (id, capacidad, rpm, electrodomestico_id) VALUES
(1, 7, 1200, 1),
(2, 8, 1300, 3),
(3, 6, 1100, 1),
(4, 9, 1400, 3);

INSERT INTO refrigerador (id, capacidad, congelador, electrodomestico_id) VALUES
(1, 350, true, 2),
(2, 300, false, 4),
(3, 400, true, 2),
(4, 280, false, 4);

INSERT INTO material (id, nombre, precio, electrodomestico_id) VALUES
(1, 'Plastic', 10.00, 1),
(2, 'Steel', 25.50, 2),
(3, 'Rubber', 7.75, 3),
(4, 'Glass', 15.30, 4);

INSERT INTO accesorio (id, nombre, fecha, electrodomestico_id) VALUES
(1, 'Filtro de agua', '2020-05-20', 1),
(2, 'Manguera extra', '2021-11-15', 2),
(3, 'Recipiente para ropa', '2019-09-30', 3),
(4, 'Cuchilla extra', '2022-03-10', 4);