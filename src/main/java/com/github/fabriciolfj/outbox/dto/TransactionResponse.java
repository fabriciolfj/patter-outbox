package com.github.fabriciolfj.outbox.dto;

import com.github.fabriciolfj.outbox.entity.TransactionStatus;
import com.github.fabriciolfj.outbox.entity.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponse(

        UUID id,
        String externalId,
        UUID accountId,
        TransactionType type,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
