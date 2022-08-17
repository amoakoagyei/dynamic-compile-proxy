package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class ProductUpdatedEventKafkaEventProcessor {

    @KafkaEventProcessor
    public void handle(ProductUpdatedEvent productUpdatedEvent, UUID correlationId) {
        System.out.println("handling ProductUpdatedEvent - " + productUpdatedEvent);
    }
}
