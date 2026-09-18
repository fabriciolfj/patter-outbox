package com.github.fabriciolfj.outbox.mapper;

import com.github.fabriciolfj.outbox.dto.TransactionRequest;
import com.github.fabriciolfj.outbox.dto.TransactionResponse;
import com.github.fabriciolfj.outbox.entity.TransactionEntity;
import com.github.fabriciolfj.outbox.entity.TransactionStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TransactionMapper {

    private static final String DEFAULT_CURRENCY = "BRL";

    public TransactionEntity toEntity(final TransactionRequest request) {
        if (request == null) {
            return null;
        }

        return TransactionEntity.builder()
                .externalId(request.externalId())
                .accountId(request.accountId())
                .type(request.type())
                .amount(request.amount())
                .currency(StringUtils.hasText(request.currency()) ? request.currency() : DEFAULT_CURRENCY)
                .description(request.description())
                .status(TransactionStatus.PENDING)
                .build();
    }

    public TransactionResponse toResponse(final TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new TransactionResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getAccountId(),
                entity.getType(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
