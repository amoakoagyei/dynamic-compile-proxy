package io.richard.event.processor.list;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.richard.event.processor.ProcessorHandlerInfo;
import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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

        methodSpecBuilder.addStatement("\treturn $L", processorCollector.getParameterCount());

        return methodSpecBuilder.build();
    }

    private MethodSpec handlerClass(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder("handleClass")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(JavaPoetHelpers.classOfAny());

        TypeName typeName = TypeName.get(processorCollector.getEnclosingElement().asType());

        methodSpecBuilder.addStatement("\treturn $T.class", typeName);

        return methodSpecBuilder.build();
    }

    private MethodSpec proxyClass(ProcessorCollector processorCollector) {
        var methodSpecBuilder = MethodSpec.methodBuilder("proxyClass")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(JavaPoetHelpers.classOfAny());

        TypeName typeName = TypeName.get(processorCollector.getEventClass().asType());

        methodSpecBuilder.addStatement("\treturn $TProxyImpl.class", typeName);

        return methodSpecBuilder.build();
    }

}
