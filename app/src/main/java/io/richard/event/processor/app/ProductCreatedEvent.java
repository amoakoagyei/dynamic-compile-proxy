package io.richard.event.processor.app;

import java.util.UUID;

public record ProductCreatedEvent(
    UUID productId,
    String name
    ) {
}
