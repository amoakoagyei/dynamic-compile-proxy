package io.richard.event.processor.app;

import java.util.Optional;

public interface DependencyInjectionAdapter {

    <T> Optional<T> getBean(Class<?> clazz);
}