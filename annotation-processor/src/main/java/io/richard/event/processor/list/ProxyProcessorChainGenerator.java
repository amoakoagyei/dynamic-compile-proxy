package io.richard.event.processor.list;

import static io.richard.event.processor.list.JavaPoetHelpers.classOfAny;
import static io.richard.event.processor.list.ProcessorProxyGenerator.PROXY_IMPL_SUFFIX;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.richard.event.processor.DependencyInjectionAdapter;
import io.richard.event.processor.EventHandlerNotFoundException;
import io.richard.event.processor.EventProcessorNotFoundException;
import io.richard.event.processor.ProcessorProxy;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class ProxyProcessorChainGenerator {

    private static final String PROXY_PROCESSOR_CLASS = "ProxyProcessorChain";

    void generateProxyProcessorChain(List<ProcessorCollector> processorCollectors, Filer filer) {

        String paramDependencyInjectionAdapter = "dependencyInjectionAdapter";
        CodeBlock.Builder staticBlockBuilder = CodeBlock.builder();
        processorCollectors.forEach(processorCollector -> {
            String className = String.format("%s%s", processorCollector.getEventClassName(), PROXY_IMPL_SUFFIX);
            TypeName typeName = TypeName.get(processorCollector.getEventClass().asType());
            staticBlockBuilder.addStatement("proxyProcessors.put($T.class, $L.class)", typeName, className);
        });

        TypeSpec.Builder processChainBuilder = TypeSpec.classBuilder(PROXY_PROCESSOR_CLASS)
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(Named.class)
                .addMember("value", "$S", StringUtil.toCamelCase(PROXY_PROCESSOR_CLASS))
                .build())
            .addAnnotation(AnnotationSpec.builder(Singleton.class).build())
            .addField(FieldSpec.builder(DependencyInjectionAdapter.class, paramDependencyInjectionAdapter)
                .addModifiers(Modifier.FINAL, Modifier.PRIVATE)
                .build())
            .addField(generateProxyProcessorsField("proxyProcessors"))
            .addStaticBlock(staticBlockBuilder.build())
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
            .beginControlFlow("if(proxyClass == null)")
            .addStatement("throw new $T(dataClass)", EventProcessorNotFoundException.class)
            .endControlFlow();

        methodSpecBuilder.addStatement("var handlerProxy = dependencyInjectionAdapter.getBean(proxyClass)")
            .addCode(CodeBlock.builder()
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
