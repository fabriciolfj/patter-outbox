package com.github.fabriciolfj.outbox.service;

import com.github.fabriciolfj.outbox.dto.TransactionRequest;
import com.github.fabriciolfj.outbox.dto.TransactionResponse;
import com.github.fabriciolfj.outbox.entity.TransactionEntity;
import com.github.fabriciolfj.outbox.mapper.OutboxMapper;
import com.github.fabriciolfj.outbox.mapper.TransactionMapper;
import com.github.fabriciolfj.outbox.repository.OutboxRepository;
import com.github.fabriciolfj.outbox.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransactionService {

    private static final String EVENT_TYPE_CREATED = "TransactionCreated";

    private final TransactionRepository transactionRepository;
    private final OutboxRepository outboxRepository;
    private final TransactionMapper transactionMapper;
    private final OutboxMapper outboxMapper;
    private final String topic;

    public TransactionService(final TransactionRepository transactionRepository,
                               final OutboxRepository outboxRepository,
                               final TransactionMapper transactionMapper,
                               final OutboxMapper outboxMapper,
                              @Value("${topic.transaction}")
                              final String topic) {
        this.transactionRepository = transactionRepository;
        this.outboxRepository = outboxRepository;
        this.transactionMapper = transactionMapper;
        this.outboxMapper = outboxMapper;
        this.topic = topic;
    }

    @Transactional
    public TransactionResponse create(final TransactionRequest request) {
        final TransactionEntity transaction = transactionRepository.save(transactionMapper.toEntity(request));
        log.info("transaction saved {}", transaction.getId());

        outboxRepository.save(outboxMapper.toOutboxEntity(transaction, EVENT_TYPE_CREATED, topic));
        log.info("outbox saved {}", transaction.getId());

        return transactionMapper.toResponse(transaction);
    }
}
