CREATE TABLE IF NOT EXISTS public.addresses (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address_id BIGINT,
    CONSTRAINT fk_clients_address
        FOREIGN KEY (address_id)
        REFERENCES public.addresses (id)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_clients_address_id ON public.clients (address_id);

CREATE TABLE IF NOT EXISTS public.phones (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT fk_phones_client
        FOREIGN KEY (client_id)
        REFERENCES public.clients (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_phones_client_id ON public.phones (client_id);

