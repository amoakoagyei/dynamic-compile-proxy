package io.richard.event.micronaut.proxy;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import io.richard.event.processor.DependencyInjectionAdapter;
import jakarta.inject.Singleton;
import java.util.Optional;

@Factory
public class DependencyInjectionAdapterFactory {

    @Singleton
    public DependencyInjectionAdapter provides(ApplicationContext applicationContext) {
        return new DependencyInjectionAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<T> getBean(Class<?> clazz) {
                Object bean = applicationContext.getBean(clazz);
                return (Optional<T>) Optional.of(bean);
            }
        };
    }
}
