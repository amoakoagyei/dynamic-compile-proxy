package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class ProductCreatedKafkaEventProcessor {

    @KafkaEventProcessor
    public void process(ProductCreatedEvent event, UUID correlationId, String partitionKey) {
        System.out.println(
            "Got Event " + event.toString() + ", Correlation: " + correlationId + ", PartitionKey: " + partitionKey);
    }
}
