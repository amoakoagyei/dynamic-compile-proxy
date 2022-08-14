package io.richard.event.processor.list;

import com.google.auto.service.AutoService;
import io.richard.event.processor.KafkaEventProcessor;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class KafkaEventProcessorProcessor extends AbstractProcessor {

    private Logger logger;
    Messager messager;
    Types typeUtils;
    Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        logger = Logger.init(KafkaEventProcessorProcessor.class, messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> kafkaEventProcessors = roundEnvironment.getElementsAnnotatedWith(
            KafkaEventProcessor.class);
        if (kafkaEventProcessors.size() == 0) {
            logger.info("Found no Kafka Event Processors on classpath");
            return true;
        }

        List<ProcessorCollector> processorCollectors = kafkaEventProcessors.stream()
            .map(it -> (ExecutableElement) it)
            .map(ProcessorCollector::of)
            .toList();

        // ensure each processor has at least one param
        List<ProcessorCollector> nullOrEmptyParamMethods = processorCollectors.stream()
            .filter(it -> it.getParameterCount() == 0)
            .toList();

        if (nullOrEmptyParamMethods.size() > 0) {
            logger.error("Found %d methods with null or 0 params", nullOrEmptyParamMethods.size());
        }

        logger.info(String.format("Found %d processors", kafkaEventProcessors.size()));

        processorCollectors.forEach(processorCollector -> {
            List<String> paramTypes = processorCollector.paramTypes();
            paramTypes.forEach(paramType -> {
                logger.info(paramType);
            });
        });

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(KafkaEventProcessor.class.getCanonicalName());
    }
}
