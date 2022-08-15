package io.richard.event.processor.list;

import static io.richard.event.processor.list.JavaPoetHelpers.classOfAny;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import io.richard.event.processor.DependencyInjectionAdapter;
import io.richard.event.processor.EventHandlerNotFoundException;
import io.richard.event.processor.ProcessorProxy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

public class ProxyProcessorChainGenerator {

    private static final String PROXY_PROCESSOR_CLASS = "ProxyProcessorChain";
    private Logger logger;

    public ProxyProcessorChainGenerator(Messager messager, Filer filer) {
        logger = Logger.init(ProxyProcessorChainGenerator.class, messager);
    }

    void generateProxyProcessorChain(List<ProcessorCollector> processorCollectors, Filer filer) {

        String paramDependencyInjectionAdapter = "dependencyInjectionAdapter";

        TypeSpec.Builder processChainBuilder = TypeSpec.classBuilder(PROXY_PROCESSOR_CLASS)
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
            .addField(FieldSpec.builder(DependencyInjectionAdapter.class, paramDependencyInjectionAdapter)
                .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                .build())
            .addField(generateProxyProcessorsField("proxyProcessors"))
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(DependencyInjectionAdapter.class, paramDependencyInjectionAdapter)
                    .build())
                .addStatement("this.$L = $L", paramDependencyInjectionAdapter, paramDependencyInjectionAdapter)
                .build())
            .addMethod(generateProcessMethod());

        JavaFile javaFile = JavaFile.builder("io.richard.event.processor", processChainBuilder.build())
            .build();
        try {
            javaFile.writeTo(filer);
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    MethodSpec generateProcessMethod() {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("process")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(JavaPoetHelpers.eventRecordOfAny(), "eventRecord");

        methodSpecBuilder.addStatement("var eventRecordData = eventRecord.data()")
            .addCode(CodeBlock.builder()
                .beginControlFlow("if(eventRecordData == null)")
                .addStatement("return")
                .endControlFlow()
                .build());

        methodSpecBuilder
            .addStatement("var dataClass = eventRecord.data().getClass()")
            .addStatement("var proxyClass = proxyProcessors.get(dataClass)")
            .addStatement("var handlerProxy = dependencyInjectionAdapter.getBean(proxyClass)");

        methodSpecBuilder.addCode(CodeBlock.builder()
            .add("var processorProxy = handlerProxy\n")
            .add("\t.map(it -> ($T)it)\n", ProcessorProxy.class)
            .add("\t.orElseThrow(() -> new $T(dataClass));\n", EventHandlerNotFoundException.class)
            .build());

        return methodSpecBuilder.addStatement("\nprocessorProxy.handle(eventRecord)")
            .build();

    }

    public static FieldSpec generateProxyProcessorsField(String fieldName) {
        return FieldSpec.builder(
                ParameterizedTypeName.get(
                    ClassName.get(Map.class),
                    classOfAny(),
                    classOfAny()
                ),
                fieldName
            )
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer(CodeBlock.builder()
                .addStatement("new $T<>()", ConcurrentHashMap.class)
                .build())
            .build();
    }
}
