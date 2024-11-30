CREATE TABLE IF NOT EXISTS schema_migrations (
    id BIGSERIAL PRIMARY KEY,
    migration_name VARCHAR(255) UNIQUE NOT NULL,
    checksum VARCHAR(64) NOT NULL,
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

