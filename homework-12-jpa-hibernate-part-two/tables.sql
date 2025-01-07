DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS price_history CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS clients CASCADE;

CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         login VARCHAR(255) NOT NULL
);

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          current_price NUMERIC(19, 2) NOT NULL
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        client_id BIGINT NOT NULL,
                        order_date DATE NOT NULL,
                        CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price_at_ordering NUMERIC(19, 2) NOT NULL,
                             CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE price_history (
                               id BIGSERIAL PRIMARY KEY,
                               product_id BIGINT NOT NULL,
                               price NUMERIC(19, 2) NOT NULL,
                               created_at TIMESTAMP NOT NULL,
                               CONSTRAINT fk_price_product FOREIGN KEY (product_id) REFERENCES products(id)
);
