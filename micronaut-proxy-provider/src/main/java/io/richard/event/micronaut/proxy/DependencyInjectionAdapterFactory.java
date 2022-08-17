package io.richard.event.micronaut.proxy;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;
import io.richard.event.processor.DependencyInjectionAdapter;
import jakarta.inject.Singleton;

@Factory
public class DependencyInjectionAdapterFactory {

    @Singleton
    public DependencyInjectionAdapter provides(ApplicationContext applicationContext) {
        return new MicronautDependencyInjectionAdapter(applicationContext);
    }
}
