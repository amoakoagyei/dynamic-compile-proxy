package io.richard.event.processor;

import java.util.Optional;

public interface DependencyInjectionAdapter {

    <T> Optional<T> getBean(Class<?> clazz);
}