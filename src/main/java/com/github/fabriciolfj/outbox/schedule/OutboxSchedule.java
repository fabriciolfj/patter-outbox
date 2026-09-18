package com.github.fabriciolfj.outbox.schedule;

import com.github.fabriciolfj.outbox.entity.OutboxEntity;
import com.github.fabriciolfj.outbox.producer.TransactionProducer;
import com.github.fabriciolfj.outbox.service.OutboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.fabriciolfj.outbox.util.GetHostnameUtil.getInstanceName;

@Slf4j
@Component
public class OutboxSchedule {

    private final TransactionProducer transactionProducer;
    private final OutboxService outboxService;
    private final int chunk;
    private final int maxAttempts;
    private final String instanceId;

    public OutboxSchedule(final TransactionProducer transactionProducer,
                           final OutboxService outboxService,
                           @Value("${chunk.value}")
                           final int chunk,
                           @Value("${outbox.max-attempts:5}")
                           final int maxAttempts) {
        this.transactionProducer = transactionProducer;
        this.outboxService = outboxService;
        this.chunk = chunk;
        this.maxAttempts = maxAttempts;
        this.instanceId = getInstanceName();
    }

    @Scheduled(fixedDelay = 10000L)
    public void publish() {
        final List<OutboxEntity> outboxes = outboxService.claimBatch(instanceId, chunk);

        if (outboxes.isEmpty()) {
            return;
        }

        log.info("publishing {} outbox event(s), instance {}", outboxes.size(), instanceId);
        outboxes.forEach(this::publish);
    }

    private void publish(final OutboxEntity outbox) {
        transactionProducer.send(outbox).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("fail publishing outbox {}", outbox.getId(), ex);
                outboxService.markAsFailed(outbox.getId(), rootCauseMessage(ex), maxAttempts);
                return;
            }

            outboxService.markAsSent(outbox.getId());
        });
    }

    private String rootCauseMessage(final Throwable ex) {
        final Throwable cause = ex.getCause();
        return cause != null ? cause.getMessage() : ex.getMessage();
    }
}
