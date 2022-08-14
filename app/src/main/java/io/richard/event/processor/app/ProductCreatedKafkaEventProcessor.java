package io.richard.event.processor.app;

import io.richard.event.processor.KafkaEventProcessor;
import java.util.UUID;

public class ProductCreatedKafkaEventProcessor {

    @KafkaEventProcessor
    public void process(ProductCreatedEvent event, UUID correlationId, String partitionKey) {

    }

    @KafkaEventProcessor
    public void process(ProductCreatedEvent event, UUID correlationId) {

    }

    @KafkaEventProcessor
    public void process(ProductCreatedEvent event) {

    }
}
