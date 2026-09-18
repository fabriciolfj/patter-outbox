package com.github.fabriciolfj.outbox.producer;

import com.github.fabriciolfj.outbox.entity.OutboxEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionProducer(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, String>> send(final OutboxEntity entity) {
        return kafkaTemplate.send(entity.getTopic(), entity.getAggregateId(), entity.getPayload());
    }
}
