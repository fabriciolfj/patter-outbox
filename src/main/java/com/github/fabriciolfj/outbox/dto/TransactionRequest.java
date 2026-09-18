package com.github.fabriciolfj.outbox.dto;

import com.github.fabriciolfj.outbox.entity.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequest(

        @NotBlank(message = "{transaction.externalId.notBlank}")
        @Size(max = 64, message = "{transaction.externalId.size}")
        String externalId,

        @NotNull(message = "{transaction.accountId.notNull}")
        UUID accountId,

        @NotNull(message = "{transaction.type.notNull}")
        TransactionType type,

        @NotNull(message = "{transaction.amount.notNull}")
        @Positive(message = "{transaction.amount.positive}")
        BigDecimal amount,

        @Size(min = 3, max = 3, message = "{transaction.currency.size}")
        String currency,

        @Size(max = 255, message = "{transaction.description.size}")
        String description
) {
}
