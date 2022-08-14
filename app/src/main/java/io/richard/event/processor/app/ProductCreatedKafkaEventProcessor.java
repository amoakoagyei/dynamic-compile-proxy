package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;
import java.util.UUID;

public class ProductCreatedKafkaEventProcessor {

    @KafkaEventProcessor
    public void process(ProductCreatedEvent event, UUID correlationId, String partitionKey) {
        System.out.println(
            "Got Event " + event.toString() + ", Correlation: " + correlationId + ", ParttionKey: " + partitionKey);
    }

//    @KafkaEventProcessor
//    public void process(ProductCreatedEvent event, UUID correlationId) {
//        System.out.println("Got Event " + event.toString() + ", Correlation: " + correlationId);
//    }
//
//    @KafkaEventProcessor
//    public void process(ProductCreatedEvent event) {
//        System.out.println("Got Event " + event.toString());
//    }
}
