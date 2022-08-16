package io.richard.event.micronaut.proxy;

import io.richard.event.processor.DependencyInjectionAdapter;
import java.util.Optional;

public class MicronautDependencyInjectionAdapter implements DependencyInjectionAdapter {
    final ApplicationContext applicationContext;

    public MicronautDependencyInjectionAdapter(
        ApplicationContext applicationContext) {this.applicationContext = applicationContext;}

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getBean(Class<?> clazz) {
        Object bean = applicationContext.getBean(clazz);
        return (Optional<T>) Optional.ofNullable(bean);
    }
}
