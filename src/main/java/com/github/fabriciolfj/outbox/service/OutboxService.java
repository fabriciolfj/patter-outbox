package com.github.fabriciolfj.outbox.service;

import com.github.fabriciolfj.outbox.entity.OutboxEntity;
import com.github.fabriciolfj.outbox.entity.OutboxStatus;
import com.github.fabriciolfj.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private static final int LAST_ERROR_MAX_LENGTH = 2000;

    private final OutboxRepository outboxRepository;

    @Transactional
    public List<OutboxEntity> claimBatch(final String instanceId, final int batchSize) {
        final var outboxes = outboxRepository.claimBatch(instanceId, batchSize);
        log.info("total outboxes {}", outboxes.size());

        return outboxes;
    }

    @Transactional
    public void markAsSent(final Long id) {
        outboxRepository.findById(id).ifPresent(outbox -> {
            outbox.setStatus(OutboxStatus.SENT);
            outbox.setSentAt(OffsetDateTime.now());
            outbox.setLockedBy(null);
            outbox.setLockedAt(null);
        });
    }

    @Transactional
    public void markAsFailed(final Long id, final String error, final int maxAttempts) {
        outboxRepository.findById(id).ifPresent(outbox -> {
            final int attempts = outbox.getAttempts() + 1;

            outbox.setAttempts(attempts);
            outbox.setLastError(truncate(error));
            outbox.setLockedBy(null);
            outbox.setLockedAt(null);
            outbox.setStatus(attempts >= maxAttempts ? OutboxStatus.DEAD : OutboxStatus.PENDING);

            log.warn("outbox {} failed, attempt {}/{}, status {}", id, attempts, maxAttempts, outbox.getStatus());
        });
    }

    private String truncate(final String error) {
        if (error == null) {
            return null;
        }

        return error.length() > LAST_ERROR_MAX_LENGTH ? error.substring(0, LAST_ERROR_MAX_LENGTH) : error;
    }
}
