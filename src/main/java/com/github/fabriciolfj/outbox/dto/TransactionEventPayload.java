package com.github.fabriciolfj.outbox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fabriciolfj.outbox.entity.TransactionStatus;
import com.github.fabriciolfj.outbox.entity.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representação serializada da transação usada como payload do evento de outbox
 * (equivalente ao {@code to_jsonb(t)} do exemplo de migração).
 */
public record TransactionEventPayload(

        UUID id,

        @JsonProperty("external_id")
        String externalId,

        @JsonProperty("account_id")
        UUID accountId,

        TransactionType type,

        BigDecimal amount,

        String currency,

        TransactionStatus status
) {
}
