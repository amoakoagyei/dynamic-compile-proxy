package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;
import jakarta.inject.Singleton;

@Singleton
public class ProductDeletedEventKafkaEventProcessor {

    @KafkaEventProcessor
    public void handleDelete(ProductDeletedEvent event) {
        System.out.println("deleting product " + event);
    }
}
