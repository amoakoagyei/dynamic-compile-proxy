package io.richard.event.processor.app;

import java.time.Instant;
import java.util.UUID;

public record ProductUpdatedEvent(UUID productId, String name, Instant timestamp) {
}
