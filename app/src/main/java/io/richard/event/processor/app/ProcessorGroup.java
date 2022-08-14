package io.richard.event.processor.app;

import io.richard.event.processor.EventRecord;
import io.richard.event.processor.ProcessorHandlerInfo;
import io.richard.event.processor.ProcessorProxy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorGroup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorGroup.class);

    private final DependencyInjectionAdapter dependencyInjectionAdapter;

    ProcessorGroup(DependencyInjectionAdapter dependencyInjectionAdapter) {
        this.dependencyInjectionAdapter = dependencyInjectionAdapter;
    }

    Map<Class<?>, ProcessorHandlerInfo> handlers = new ConcurrentHashMap<>();

    void process(EventRecord<ProductCreatedEvent> eventRecord) {
        if (eventRecord.data() == null) {
            return;
        }

        Class<?> dataClass = eventRecord.data().getClass();
        ProcessorHandlerInfo handlerInfo = findHandlers(dataClass);
        if (handlerInfo == null) {
            return;
        }

        Optional<Object> bean = dependencyInjectionAdapter.getBean(handlerInfo.handleClass());
        Object handlerProcessor = bean.orElseThrow(() -> new EventHandlerNotFoundException(dataClass));

        ProcessorProxy itemProcessorChain = new ProductCreatedEventProcessorProxy(handlerProcessor);
        itemProcessorChain.process(handlerInfo, eventRecord);
    }

    private ProcessorHandlerInfo findHandlers(Class<?> dataClass) {
        return handlers.get(dataClass);
    }

    public void register(Class<?> clzz, ProcessorHandlerInfo processorHandlerInfo){
        handlers.put(clzz, processorHandlerInfo);
    }
}
