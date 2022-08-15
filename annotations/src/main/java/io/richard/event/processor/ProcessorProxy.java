package io.richard.event.processor;

@FunctionalInterface
public interface ProcessorProxy {

     void handle(EventRecord<?> eventRecord);
}
