package io.richard.event.processor;

public interface ProcessorHandlerInfo {

    int paramCount();

    Class<?> handleClass();


}
