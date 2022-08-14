package io.richard.event.processor.app;

import java.util.UUID;

public class ItemProcessorChain implements ProcessorChain {

    private final Object delegate;

    public ItemProcessorChain(Object handlerProcessor) {
        this.delegate = handlerProcessor;
    }

    @Override
    public void handle(Object object) {
        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object);
    }

    @Override
    public void handle(Object object, UUID correlationId) {
        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object, correlationId);
    }

    @Override
    public void handle(Object object, UUID correlationId, String partitionKey) {
        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object, correlationId,
            partitionKey);
    }
}
