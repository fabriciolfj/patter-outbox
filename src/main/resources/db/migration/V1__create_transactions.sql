CREATE TABLE transactions (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    external_id     VARCHAR(64)     NOT NULL,          -- chave de idempotência vinda do cliente
    account_id      UUID            NOT NULL,
    type            VARCHAR(20)     NOT NULL,
    amount          NUMERIC(19, 2)  NOT NULL,
    currency        CHAR(3)         NOT NULL DEFAULT 'BRL',
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    description     VARCHAR(255),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    version         BIGINT          NOT NULL DEFAULT 0, -- optimistic locking (@Version)

    CONSTRAINT uk_transactions_external_id UNIQUE (external_id),
    CONSTRAINT ck_transactions_amount      CHECK (amount > 0),
    CONSTRAINT ck_transactions_type        CHECK (type IN ('CREDIT', 'DEBIT')),
    CONSTRAINT ck_transactions_status      CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'))
);

CREATE INDEX idx_transactions_account_created ON transactions (account_id, created_at DESC);

CREATE OR REPLACE FUNCTION set_updated_at() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_transactions_updated_at
    BEFORE UPDATE ON transactions
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
