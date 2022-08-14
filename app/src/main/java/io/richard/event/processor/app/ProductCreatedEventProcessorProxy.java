package io.richard.event.processor.app;

import io.richard.event.processor.ProcessorProxy;
import java.util.UUID;

public class ProductCreatedEventProcessorProxy implements ProcessorProxy {

    private final Object delegate;

    public ProductCreatedEventProcessorProxy(Object handlerProcessor) {
        this.delegate = handlerProcessor;
    }

    @Override
    public void handle(Object object) {
//        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object);
    }

    @Override
    public void handle(Object object, UUID correlationId) {
//        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object, correlationId);
    }

    @Override
    public void handle(Object object, UUID correlationId, String partitionKey) {
        ((ProductCreatedKafkaEventProcessor) delegate).process((ProductCreatedEvent) object, correlationId,
            partitionKey);
    }
}
