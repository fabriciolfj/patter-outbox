package com.github.fabriciolfj.outbox.mapper;

import com.github.fabriciolfj.outbox.dto.TransactionEventPayload;
import com.github.fabriciolfj.outbox.entity.OutboxEntity;
import com.github.fabriciolfj.outbox.entity.OutboxStatus;
import com.github.fabriciolfj.outbox.entity.TransactionEntity;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Component
public class OutboxMapper {

    private static final String AGGREGATE_TYPE = "Transaction";

    private final JsonMapper jsonMapper;

    public OutboxMapper(final JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public OutboxEntity toOutboxEntity(final TransactionEntity transaction, final String eventType, final String topic) {
        if (transaction == null) {
            return null;
        }

        return OutboxEntity.builder()
                .aggregateType(AGGREGATE_TYPE)
                .aggregateId(transaction.getId().toString())
                .eventType(eventType)
                .topic(topic)
                .payload(writePayload(transaction))
                .status(OutboxStatus.PENDING)
                .attempts(0)
                .build();
    }

    private String writePayload(final TransactionEntity transaction) {
        final TransactionEventPayload payload = new TransactionEventPayload(
                transaction.getId(),
                transaction.getExternalId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus()
        );

        try {
            return jsonMapper.writeValueAsString(payload);
        } catch (JacksonException e) {
            throw new IllegalStateException("Falha ao serializar payload do evento de outbox", e);
        }
    }
}
