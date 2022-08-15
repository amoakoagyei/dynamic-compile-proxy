package io.richard.event.processor.list;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.richard.event.processor.ProcessorProxy;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

public class ProcessorProxyGenerator {

    private final Logger logger;
    private final Filer filer;

    private static final String DELEGATE_FIELD = "delegate";
    private static final String HANDLE_METHOD_NAME = "handle";
    private static final String EVENT_RECORD_DATA_VARIABLE = "eventRecordData";
    private static final String EVENT_RECORD_PARAM = "eventRecord";
    private static final String PARTITION_KEY = "partitionKey";
    private static final String CORRELATION_ID = "correlationId";
    private static final String PROXY_IMPL_SUFFIX = "ProxyImpl";

    public ProcessorProxyGenerator(Messager messager, Filer filer) {
        logger = Logger.init(ProcessorProxyGenerator.class, messager);
        this.filer = filer;
    }

    public void generate(ProcessorCollector processorCollector) throws IOException {
        String className = String.format("%s%s", processorCollector.getEventClassName(), PROXY_IMPL_SUFFIX);

        TypeName delegateTypeName = TypeName.get(processorCollector.getEnclosingElement().asType());
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ProcessorProxy.class)
            .addField(FieldSpec.builder(delegateTypeName, DELEGATE_FIELD)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build())
            .addMethod(generateConstructor(delegateTypeName))
            .addMethod(handleMethod(processorCollector));

        JavaFile javaFile = JavaFile.builder("io.richard.event.processor", typeSpec.build())
            .build();
        javaFile.writeTo(filer);
        javaFile.writeTo(System.out);
    }

    private MethodSpec generateConstructor(TypeName delegateTypeName) {

        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ParameterSpec.builder(delegateTypeName, DELEGATE_FIELD)
                .build())
            .addStatement(
                "this.$L = $L", DELEGATE_FIELD, DELEGATE_FIELD)
            .build();
    }

    private MethodSpec handleMethod(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder(HANDLE_METHOD_NAME)
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(JavaPoetHelpers.eventRecordOfAny(), EVENT_RECORD_PARAM);

        VariableElement variableElement = processorCollector.getEventClass();
        methodSpecBuilder.addStatement(
            "var $L = ($T)$L.data()",
            EVENT_RECORD_DATA_VARIABLE,
            TypeName.get(variableElement.asType()),
            EVENT_RECORD_PARAM
        );

        if (processorCollector.getParameterCount() == 3) {
            methodSpecBuilder.addStatement(
                    "var $L = $L.$L()",
                    CORRELATION_ID,
                    EVENT_RECORD_PARAM,
                    CORRELATION_ID
                )
                .addStatement(
                    "var $L = $L.$L()",
                    PARTITION_KEY,
                    EVENT_RECORD_PARAM,
                    PARTITION_KEY
                );
            methodSpecBuilder.addStatement(
                "this.$L.$L($L, $L, $L)",
                DELEGATE_FIELD,
                processorCollector.getElementName(),
                EVENT_RECORD_DATA_VARIABLE,
                CORRELATION_ID,
                PARTITION_KEY
            );
        }

        if (processorCollector.getParameterCount() == 2) {
            methodSpecBuilder.addStatement(
                "var $L = $L.$L()",
                CORRELATION_ID,
                EVENT_RECORD_PARAM,
                CORRELATION_ID
            );
            methodSpecBuilder.addStatement(
                "this.$L.$L($L, $L)",
                DELEGATE_FIELD,
                processorCollector.getElementName(),
                EVENT_RECORD_DATA_VARIABLE,
                CORRELATION_ID
            );
        }

        return methodSpecBuilder.build();
    }
}
