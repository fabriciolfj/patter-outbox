package com.github.fabriciolfj.outbox.repository;

import com.github.fabriciolfj.outbox.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {


}
