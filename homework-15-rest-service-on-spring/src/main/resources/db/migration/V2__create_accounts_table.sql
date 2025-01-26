CREATE TABLE accounts (
    id VARCHAR(36) PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    client_id VARCHAR(255),
    balance INT,
    is_blocked BOOLEAN NOT NULL
);

INSERT INTO accounts (id, account_number, client_id, balance, is_blocked) VALUES
    ('1a2b3c4d-1234-5678-9abc-001122334455', '000000000001', '1000000001', 1000, FALSE),
    ('5e6f7g8h-2345-6789-abcd-112233445566', '000000000002', '1000000002', 500, FALSE);