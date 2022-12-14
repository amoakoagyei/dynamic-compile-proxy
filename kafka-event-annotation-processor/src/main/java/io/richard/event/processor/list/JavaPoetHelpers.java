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
}
