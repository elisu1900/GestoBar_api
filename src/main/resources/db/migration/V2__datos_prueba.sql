    -- ============================================================
    -- V2__datos_prueba.sql
    -- Datos de prueba para entorno de desarrollo
    -- ============================================================

    -- ------------------------------------------------------------
    -- CATEGORIES
    -- ------------------------------------------------------------
    INSERT INTO categories (category_id, name) VALUES
    (1,'Entrantes'),
    (2,'Platos principales'),
    (3,'Postres'),
    (4, 'Bebidas'),
    (5, 'Menú del día');

    -- ------------------------------------------------------------
    -- PRODUCTS
    -- ------------------------------------------------------------
    INSERT INTO products (product_id, category_id, name, price_sell, price_cost, is_active) VALUES
-- Entrantes
(1,  1, 'Croquetas de jamón (6 ud)',     7.50,  3.00,  true),
(2,  1, 'Patatas bravas',                5.00,  1.50,  true),
(3,  1, 'Tabla de ibéricos',            14.00,  6.00,  true),
-- Platos principales
(4,  2, 'Secreto ibérico a la brasa',   16.50,  7.00,  true),
(5,  2, 'Merluza en salsa verde',       15.00,  6.50,  true),
(6,  2, 'Carrillada de cerdo',          14.50,  5.50,  true),
(7,  2, 'Hamburguesa premium',          13.00,  4.50,  true),
-- Postres
(8,  3, 'Tarta de queso',               5.50,  1.80,  true),
(9,  3, 'Brownie con helado',           5.00,  1.50,  true),
(10, 3, 'Flan casero',                  4.00,  1.00,  true),
-- Bebidas
(11, 4, 'Agua mineral 50cl',            1.50,  0.30,  true),
(12, 4, 'Refresco lata',                2.50,  0.60,  true),
(13, 4, 'Cerveza caña',                 2.00,  0.50,  true),
(14, 4, 'Vino de la casa (copa)',        2.50,  0.80,  true),
(15, 4, 'Zumo natural',                 3.00,  0.90,  true),
-- Menú del día
(16, 5, 'Menú completo',               12.00,  5.00,  false);
    -- ------------------------------------------------------------
    -- RESTAURANT_TABLES
    -- ------------------------------------------------------------
    INSERT INTO restaurant_tables (table_id, number, capacity, is_active) VALUES
    (1,  1,  2, true),
    (2,  2,  2, true),
    (3,  3,  4, true),
    (4,  4,  4, true),
    (5,  5,  6, true),
    (6,  6,  6, true),
    (7,  7,  8, true),
    (8,  8,  8, true),
    (9,  9,  4, true),
    (10, 10, 2, false); -- mesa inactiva / en mantenimiento

    -- ------------------------------------------------------------
    -- USERS
    -- Las contraseñas son BCrypt de "password123"
    -- ------------------------------------------------------------
    INSERT INTO users (user_id, name, last_name, password, role, is_active, created_at) VALUES
    (1, 'Admin',    'Sistema',    '$2a$10$WgO9c8H3vAn8zWB.V3gC1O3dSeOBYwVcpUsXLEU3Dge761DrWmoFu', 'ADMIN',   true,  NOW()),
    (2, 'Carlos',   'García',     '$2a$10$EaT37zd5uo5RXh2.4TExqeudYadFoOVAwUX0G6Ih9QpdbzYP83lZO', 'WAITER',  true,  NOW()),
    (3, 'María',    'López',      '$2a$10$EaT37zd5uo5RXh2.4TExqeudYadFoOVAwUX0G6Ih9QpdbzYP83lZO', 'WAITER',  true,  NOW()),
    (4, 'Pedro',    'Martínez',   '$2a$10$EaT37zd5uo5RXh2.4TExqeudYadFoOVAwUX0G6Ih9QpdbzYP83lZO', 'WAITER', true,  NOW()),
    (5, 'Lucía',    'Fernández',  '$2a$10$EaT37zd5uo5RXh2.4TExqeudYadFoOVAwUX0G6Ih9QpdbzYP83lZO', 'WAITER',  false, NOW()); -- usuario inactivo

    -- ------------------------------------------------------------
    -- TICKETS
    -- status: OPEN, CLOSED, CANCELLED (ajusta a tus enums)
    -- ------------------------------------------------------------
    INSERT INTO tickets (ticket_id, user_id, table_id, status, total, created_at, closed_at) VALUES
    -- Ticket cerrado (pagado)
    (1, 2, 3, 'CLOSED',    45.50, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '1 hour'),
    -- Ticket abierto (mesa ocupada ahora mismo)
    (2, 3, 5, 'OPEN',      32.00, NOW() - INTERVAL '30 minutes', NULL),
    -- Otro ticket abierto
    (3, 2, 1, 'OPEN',      10.00, NOW() - INTERVAL '15 minutes', NULL),
    -- Ticket cancelado
    (4, 3, 4, 'CLOSED',  0.00, NOW() - INTERVAL '3 hours',   NOW() - INTERVAL '3 hours');

    -- ------------------------------------------------------------
    -- TICKET_DETAILS
    -- ------------------------------------------------------------
    -- Detalles ticket 1 (cerrado) - mesa 3
    INSERT INTO ticket_details (ticket_id, product_id, quantity, unit_price) VALUES
    (1, 1,  2,  7.50),   -- 2x Croquetas
    (1, 4,  2, 16.50),   -- 2x Secreto ibérico
    (1, 8,  1,  5.50),   -- 1x Tarta de queso
    (1, 13, 4,  2.00);   -- 4x Cervezas

    -- Detalles ticket 2 (abierto) - mesa 5
    INSERT INTO ticket_details (ticket_id, product_id, quantity, unit_price) VALUES
    (2, 2,  1,  5.00),   -- 1x Patatas bravas
    (2, 6,  2, 14.50),   -- 2x Carrillada
    (2, 14, 2,  2.50);   -- 2x Vino copa

    -- Detalles ticket 3 (abierto) - mesa 1
    INSERT INTO ticket_details (ticket_id, product_id, quantity, unit_price) VALUES
    (3, 11, 2,  1.50),   -- 2x Agua
    (3, 12, 2,  2.50),   -- 2x Refresco
    (3, 9,  1,  5.00);   -- 1x Brownie
