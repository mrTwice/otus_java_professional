CREATE TABLE transfers (
    id VARCHAR(36) PRIMARY KEY,
    client_id VARCHAR(10),
    target_client_id VARCHAR(10),
    source_account VARCHAR(12),
    target_account VARCHAR(12),
    amount INT,
    message VARCHAR(255)
);

INSERT INTO transfers (id, client_id, target_client_id, source_account, target_account, amount, message) VALUES
    ('bde76ffa-f133-4c23-9bca-03618b2a94b2', '1000000001', '1000000002', '000000000001', '000000000002', 100, 'Тестовый перевод'),
    ('32ebb2eb-ed35-4baa-b500-b7f6535e4c88', '1000000002', '1000000001', '000000000002', '000000000001', 50, 'Обратный тестовый перевод');
