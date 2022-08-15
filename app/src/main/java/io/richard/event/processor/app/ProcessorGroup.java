package io.richard.event.processor.app;

import io.richard.event.processor.DependencyInjectionAdapter;
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

    void process(EventRecord<ProductCreatedEvent> eventRecord) {
        if (eventRecord.data() == null) {
            return;
        }

        Class<?> dataClass = eventRecord.data().getClass();
        Class<?> proxyClass = proxyProcessors.get(dataClass);

        Optional<Object> handlerProxy = dependencyInjectionAdapter.getBean(proxyClass);
        ProcessorProxy processorProxy = handlerProxy.map(it -> (ProcessorProxy)it).orElseThrow(() -> new EventHandlerNotFoundException(dataClass));
        processorProxy.handle(eventRecord);

//        ProcessorProxy itemProcessorChain = new ProductCreatedEventProxyImpl(handlerProcessor);
//        itemProcessorChain.handle(eventRecord);
//        if (handlerInfo.paramCount() == 3) {
//            itemProcessorChain.handle(eventRecord.data(), eventRecord.correlationId(), eventRecord.partitionKey());
//        } else if (handlerInfo.paramCount() == 2) {
//            itemProcessorChain.handle(eventRecord.data(), eventRecord.correlationId(), "");
//        } else if (handlerInfo.paramCount() == 1) {
//            itemProcessorChain.handle(eventRecord.data(), null, "");
//        } else {
        // handler methods should have params between 1 and 3
//                    and should be in the order of (event, correlationId, partitionKey)
        // event can be any class object
        // correlationId is expected to be UUID
        // partition
//            throw new RuntimeException("HandlerParam Count is out of range of methods");
//        }
    }


    public void registerProxy(Class<?> clzz, Class<?> proxyClass) {
        proxyProcessors.put(clzz, proxyClass);
    }
}
