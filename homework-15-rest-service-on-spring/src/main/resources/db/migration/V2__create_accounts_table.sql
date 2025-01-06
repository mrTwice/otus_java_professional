CREATE TABLE accounts (
    id VARCHAR(36) PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    client_id VARCHAR(255),
    balance INT,
    is_blocked BOOLEAN NOT NULL
);