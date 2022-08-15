package io.richard.event.processor.app;

import java.util.UUID;

public record ProductDeletedEvent(UUID productId) {
}
