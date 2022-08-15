package io.richard.event.processor.app;

import io.richard.event.processor.annotations.KafkaEventProcessor;

public class ProductDeletedEventKafkaEventProcessor {

    @KafkaEventProcessor
    public void handleDelete(ProductDeletedEvent event) {
        System.out.println("deleting product " + event);
    }
}
