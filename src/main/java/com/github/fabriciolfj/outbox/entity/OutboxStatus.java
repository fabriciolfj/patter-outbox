package com.github.fabriciolfj.outbox.entity;

public enum OutboxStatus {

    PENDING,
    PROCESSING,
    SENT,
    DEAD
}
