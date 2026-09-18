CREATE TABLE outbox (
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aggregate_type  VARCHAR(50)     NOT NULL,          -- ex: 'Transaction'
    aggregate_id    VARCHAR(64)     NOT NULL,          -- usado como key no Kafka
    event_type      VARCHAR(100)    NOT NULL,          -- ex: 'TransactionCreated'
    topic           VARCHAR(255)    NOT NULL,
    payload         JSONB           NOT NULL,
    headers         JSONB,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    attempts        INT             NOT NULL DEFAULT 0,
    last_error      TEXT,
    locked_by       VARCHAR(64),
    locked_at       TIMESTAMPTZ,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    sent_at         TIMESTAMPTZ,

    CONSTRAINT ck_outbox_status CHECK (status IN ('PENDING', 'PROCESSING', 'SENT', 'DEAD'))
);

-- Índices parciais: cobrem só o que o poller consulta, ficam pequenos
CREATE INDEX idx_outbox_pending    ON outbox (created_at) WHERE status = 'PENDING';
CREATE INDEX idx_outbox_processing ON outbox (locked_at)  WHERE status = 'PROCESSING';
