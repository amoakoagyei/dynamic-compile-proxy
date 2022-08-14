package io.richard.event.processor.app;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    static final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    public void put(Object o) {
        beans.put(o.getClass(), o);
    }
}
