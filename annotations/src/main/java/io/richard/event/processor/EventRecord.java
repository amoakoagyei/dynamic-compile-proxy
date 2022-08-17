package io.richard.event.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
Header:
CorrelationId:
source:
PartitionKey:
ObjectType:
SimpleObjectType:
timestamp
id

{

}

if (this.objectType != null) {
            return this.ObjectType.replaceFirst(".*\\.", "");
        }
        return null;
 */

public record EventRecord<T>(
    T data,
    UUID correlationId,
    String partitionKey,
    Map<String, Object> headers
) {

    private static final String CORRELATION_ID_KEY = "correlationId";
    private static final String PARTITION_KEY = "partitionKey";

    public EventRecord {
        if (headers == null) {
            headers = new HashMap<>();
        }else{
            headers = new HashMap<>(headers);
        }

        if (correlationId != null) {
            headers.put(CORRELATION_ID_KEY, correlationId);
        }
        if (partitionKey != null) {
            headers.put(PARTITION_KEY, correlationId);
        }
    }

    public EventRecord(T data, UUID correlationId, String partitionKey) {
        this(data, correlationId, partitionKey, Map.of(
            CORRELATION_ID_KEY, correlationId,
            PARTITION_KEY, partitionKey
        ));
    }
}
