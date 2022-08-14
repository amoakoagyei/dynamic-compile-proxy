package io.richard.event.processor.app;

import java.util.UUID;

public interface ProcessorChain {

    default <T> void process(ProcessorHandlerInfo handlerInfo, EventRecord<T> eventRecord) {
        if (handlerInfo.paramCount() == 3) {
            handle(eventRecord.data, eventRecord.correlationId, eventRecord.partitionKey);
        } else if (handlerInfo.paramCount() == 2) {
            handle(eventRecord.data, eventRecord.correlationId);
        } else if (handlerInfo.paramCount() == 1) {
            handle(eventRecord.data);
        } else {
            // handler methods should have params between 1 and 3
//                    and should be in the order of (event, correlationId, partitionKey)
            // event can be any class object
            // correlationId is expected to be UUID
            // partition
            throw new RuntimeException("HandlerParam Count is out of range of methods");
        }
    }

    void handle(Object object);

    void handle(Object object, UUID correlationId);

    void handle(Object object, UUID correlationId, String partitionKey);
}
