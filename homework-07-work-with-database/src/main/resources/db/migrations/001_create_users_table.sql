CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    user_login VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_nickname VARCHAR(255) NOT NULL
);