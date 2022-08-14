/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("io.richard.event.processor.java-library-conventions")
}

dependencies {
    implementation(project(":annotations"))
    implementation("com.squareup:javapoet:1.13.0")
    annotationProcessor("com.google.auto.service:auto-service:1.0.1")
    compileOnly("com.google.auto.service:auto-service:1.0.1")
}
