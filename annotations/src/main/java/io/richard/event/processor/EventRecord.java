package io.richard.event.processor;

import static io.richard.event.processor.EventRecordHeaders.CORRELATION_ID;
import static io.richard.event.processor.EventRecordHeaders.OBJECT_TYPE;
import static io.richard.event.processor.EventRecordHeaders.PARTITION_KEY;
import static io.richard.event.processor.EventRecordHeaders.SIMPLE_OBJECT_TYPE;
import static io.richard.event.processor.EventRecordHeaders.TIMESTAMP;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record EventRecord<T>(
    T data,
    UUID correlationId,
    String partitionKey,
    Map<String, Object> headers
) {

    public EventRecord {
        if (headers == null) {
            headers = new HashMap<>();
        } else {
            headers = new HashMap<>(headers);
        }

        if (correlationId != null) {
            headers.put(CORRELATION_ID, correlationId);
        }
        if (partitionKey != null) {
            headers.put(PARTITION_KEY, partitionKey);
        }

        String objectType = data.getClass().getCanonicalName();
        headers.computeIfAbsent(OBJECT_TYPE, k -> objectType);
        headers.computeIfAbsent(SIMPLE_OBJECT_TYPE, k -> objectType.replaceFirst(".*\\.", ""));
        headers.computeIfAbsent(TIMESTAMP, k -> Instant.now().toString());
    }

    public EventRecord(T data, UUID correlationId, String partitionKey) {
        this(data, correlationId, partitionKey, Map.of(
            CORRELATION_ID, correlationId,
            PARTITION_KEY, partitionKey
        ));
    }
}
