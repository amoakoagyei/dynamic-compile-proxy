package io.richard.event.processor.list;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ProcessorCollector {

    private ExecutableElement element;
    private TypeElement enclosingElement;
    private int parameterCount;
    private List<String> paramTypes = List.of();
    private String enclosingElementName = "";
    private String eventClassName = "";
    private VariableElement eventClass;

    public static ProcessorCollector of(ExecutableElement it) {
        var processorCollector = new ProcessorCollector()
            .withEnclosingElement((TypeElement) it.getEnclosingElement())
            .withElement(it);

        List<? extends VariableElement> parameters = it.getParameters();
        if (parameters == null || parameters.size() == 0) {
            return processorCollector;
        }

        VariableElement variableElement = it.getParameters().get(0);
        return processorCollector
            .withParameterCount(parameters.size())
            .withEventClass(variableElement)
            .withEventClassName(variableElement.asType().toString())
            .withParamTypes(it.getParameters().stream()
                .map(param -> param.asType().toString())
                .toList());
    }

    private ProcessorCollector withEventClass(VariableElement eventClass) {
        this.eventClass = eventClass;
        return this;
    }

    private ProcessorCollector withEventClassName(String fullEventClassName) {
        String[] split = fullEventClassName.split("\\.");
        this.eventClassName = split[split.length - 1];
        return this;
    }

    private ProcessorCollector withEnclosingElement(TypeElement enclosingElement) {
        this.enclosingElement = enclosingElement;
        this.enclosingElementName = enclosingElement.asType().toString();
        return this;
    }

    private ProcessorCollector withParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
        return this;
    }

    private ProcessorCollector withParameterCount(int paramCount) {
        this.parameterCount = paramCount;
        return this;
    }

    private ProcessorCollector withElement(ExecutableElement it) {
        this.element = it;
        return this;
    }

    public ExecutableElement getElement() {
        return element;
    }

    public TypeElement getEnclosingElement() {
        return enclosingElement;
    }

    public List<String> getParameterTypes() {
        return paramTypes;
    }

    /**
     * @return Method Name
     */
    public String getElementName() {
        return element.getSimpleName().toString();
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public String getEnclosingElementName() {
        return enclosingElementName;
    }

    public String getEventClassName() {
        return eventClassName;
    }
}
