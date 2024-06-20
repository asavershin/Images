package com.github.asavershin.api.common.domain;

public interface DomainEvent<T> {
    /**
     * Using for publishing new events.
     * @return Message for publisher
     */
    T publish();
}
