package io.richard.event.processor.list;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.richard.event.processor.ProcessorHandlerInfo;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

public class ProcessorHandlerInfoGenerator {

    private final Logger logger;
    private final Filer filer;

    public ProcessorHandlerInfoGenerator(Messager messager, Filer filer) {
        logger = Logger.init(ProcessorHandlerInfoGenerator.class, messager);
        this.filer = filer;
    }

    public void generate(ProcessorCollector processorCollector) throws IOException {
        String className = String.format("%sProcessorHandlerInfoImpl", processorCollector.getEventClassName());
        logger.info("generating ProcessorHandlerInfo for %s", processorCollector.getEnclosingElementName());
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ProcessorHandlerInfo.class)
            .addAnnotation(AnnotationSpec.builder(Named.class)
                .addMember("value", "$S", StringUtil.toCamelCase(className))
                .build())
            .addAnnotation(AnnotationSpec.builder(Singleton.class).build())
            .addMethod(paramCount(processorCollector))
            .addMethod(handlerClass(processorCollector))
            .addMethod(proxyClass(processorCollector));

        JavaFile javaFile = JavaFile.builder("io.richard.event.processor", typeSpec.build())
            .build();
        javaFile.writeTo(filer);
    }

    private MethodSpec paramCount(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder("paramCount")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.INT);

        return methodSpecBuilder
            .addStatement("\treturn $L", processorCollector.getParameterCount())
            .build();
    }

    private MethodSpec handlerClass(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder("handleClass")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(JavaPoetHelpers.classOfAny());

        return methodSpecBuilder
            .addStatement("\treturn $T.class", TypeName.get(processorCollector.getEnclosingElement().asType()))
            .build();
    }

    private MethodSpec proxyClass(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder("proxyClass")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(JavaPoetHelpers.classOfAny());

        return methodSpecBuilder
            .addStatement("\treturn $TProxyImpl.class", TypeName.get(processorCollector.getEventClass().asType()))
            .build();
    }

}
