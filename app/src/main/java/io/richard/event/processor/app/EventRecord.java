package io.richard.event.processor.app;

import java.util.UUID;

public class EventRecord<T> {

    final T data;
    final UUID correlationId;
    final String partitionKey;

    public EventRecord(T data, UUID correlationId, String partitionKey) {
        this.data = data;
        this.correlationId = correlationId;
        this.partitionKey = partitionKey;
    }
}
