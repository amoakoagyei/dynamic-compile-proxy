package io.richard.event.processor.app;

import io.richard.event.processor.DependencyInjectionAdapter;
import io.richard.event.processor.EventHandlerNotFoundException;
import io.richard.event.processor.EventRecord;
import io.richard.event.processor.ProcessorProxy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorGroup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorGroup.class);

    Map<Class<?>, Class<?>> proxyProcessors = new ConcurrentHashMap<>();

    private final DependencyInjectionAdapter dependencyInjectionAdapter;

    ProcessorGroup(DependencyInjectionAdapter dependencyInjectionAdapter) {
        this.dependencyInjectionAdapter = dependencyInjectionAdapter;
    }

    void process(EventRecord<?> eventRecord) {
        if (eventRecord.data() == null) {
            return;
        }

        Class<?> dataClass = eventRecord.data().getClass();
        Class<?> proxyClass = proxyProcessors.get(dataClass);

        Optional<Object> handlerProxy = dependencyInjectionAdapter.getBean(proxyClass);
        ProcessorProxy processorProxy = handlerProxy
            .map(it -> (ProcessorProxy)it)
            .orElseThrow(() -> new EventHandlerNotFoundException(dataClass));
        processorProxy.handle(eventRecord);
    }


    public void registerProxy(Class<?> clzz, Class<?> proxyClass) {
        proxyProcessors.put(clzz, proxyClass);
    }
}
