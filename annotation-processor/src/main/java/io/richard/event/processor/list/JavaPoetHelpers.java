package io.richard.event.processor.list;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import io.richard.event.processor.EventRecord;

public class JavaPoetHelpers {

    public static ParameterizedTypeName classOfAny() {
        TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);

        return ParameterizedTypeName.get(
            ClassName.get(Class.class), wildcard);
    }

    public static ParameterizedTypeName eventRecordOfAny() {
        TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);

        return ParameterizedTypeName.get(
            ClassName.get(EventRecord.class), wildcard);
    }

//    public static ParameterizedTypeName getParameterizedAggregateRoot() {
//        TypeName aggregateIdWildCard = WildcardTypeName.subtypeOf(AggregateId.class);
//        return ParameterizedTypeName.get(
//            ClassName.get(AbstractAggregateRoot.class), aggregateIdWildCard,
//            ClassName.get(VersionedEvent.class));
//    }

//    public static FieldSpec generateEventsApplierMapField(String fieldName) {
//        return FieldSpec.builder(
//                ParameterizedTypeName.get(
//                    ClassName.get(Map.class),
//                    classOfAny(),
//                    eventApplierBeanName
//                ),
//                fieldName
//            )
//            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//            .build();
//    }
}
