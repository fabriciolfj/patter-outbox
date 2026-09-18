package com.github.fabriciolfj.outbox.repository;

import com.github.fabriciolfj.outbox.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEntity, Long> {

    @Modifying
    @Query(value = """
    UPDATE outbox
    SET status = 'PROCESSING', locked_by = :instanceId, locked_at = now()
    WHERE id IN (
        SELECT id FROM outbox
        WHERE status = 'PENDING'
           OR (status = 'PROCESSING' AND locked_at < now() - interval '2 minutes')
        ORDER BY created_at
        LIMIT :batchSize
        FOR UPDATE SKIP LOCKED
    )
    RETURNING *
    """, nativeQuery = true)
    List<OutboxEntity> claimBatch(@Param("instanceId") String instanceId,
                                  @Param("batchSize") int batchSize);

}
