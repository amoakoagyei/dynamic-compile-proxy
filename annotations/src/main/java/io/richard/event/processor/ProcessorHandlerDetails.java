package io.richard.event.processor;

public record ProcessorHandlerDetails(
    int methodParamCount,
    Class<?> handlerProxy
) {
}
