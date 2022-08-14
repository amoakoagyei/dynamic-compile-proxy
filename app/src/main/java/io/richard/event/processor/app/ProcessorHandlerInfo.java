package io.richard.event.processor.app;

public interface ProcessorHandlerInfo {

    int paramCount();

    Class<?> handleClass();


}
