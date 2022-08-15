package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;
import java.util.UUID;

public class ProductUpdatedEventKafkaEventProcessor {

    @KafkaEventProcessor
    public void handle(ProductUpdatedEvent productUpdatedEvent, UUID correlationId) {
        System.out.println("handling ProductUpdatedEvent - " + productUpdatedEvent);
    }
}
