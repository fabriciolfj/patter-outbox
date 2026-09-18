package com.github.fabriciolfj.outbox.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionConsumer {

    @KafkaListener(topics = "${topic.transaction}")
    public void receive(final String json, final Acknowledgment ack) {
        try {
            log.info("message receive {}", json);
        } finally {
            ack.acknowledge();
        }

    }
}
