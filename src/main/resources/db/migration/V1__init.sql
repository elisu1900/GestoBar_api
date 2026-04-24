CREATE SCHEMA IF NOT EXISTS gestobar;
SET search_path TO gestobar;

-- Usuarios (waiter, admin)
CREATE TABLE users
(
    user_id   SERIAL PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    role      VARCHAR(50)  NOT NULL DEFAULT 'WAITER', -- 'WAITER' OR 'ADMIN'
    is_active BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Categorías de productos
CREATE TABLE categories
(
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);

-- Productos
CREATE TABLE products
(
    product_id  SERIAL PRIMARY KEY,
    category_id INT            REFERENCES categories (category_id),
    name        VARCHAR(255)   NOT NULL,
    price_sell       DECIMAL(10, 2) NOT NULL,
    price_cost       DECIMAL(10, 2) NOT NULL,
    is_active   BOOLEAN        NOT NULL DEFAULT TRUE
);

-- Mesas
CREATE TABLE restaurant_tables
(
    table_id  SERIAL PRIMARY KEY,
    number    INT     NOT NULL UNIQUE,
    capacity  INT     NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE
);

-- Tickets (comandas)
CREATE TABLE tickets
(
    ticket_id  SERIAL PRIMARY KEY,
    table_id   INT         REFERENCES restaurant_tables (table_id),
    user_id    INT         REFERENCES users (user_id),
    status     VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    total      DECIMAL(10, 2)       DEFAULT 0.00,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at  TIMESTAMP
);

-- Detalle de tickets
CREATE TABLE ticket_details
(
    ticket_id  INT            NOT NULL REFERENCES tickets (ticket_id),
    product_id INT            NOT NULL REFERENCES products (product_id),
    quantity   INT            NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (ticket_id, product_id)
);