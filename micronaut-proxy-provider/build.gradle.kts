//plugins {
//    id("java")
//}
plugins {
    id("io.richard.event.processor.java-library-conventions")
//    id 'java-library'
    id("io.micronaut.library") version "2.0.8"
}

dependencies {
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:slf4j-simple")

    annotationProcessor("io.micronaut:micronaut-inject-java")
    implementation("io.micronaut:micronaut-inject-java")

    /*
    annotationProcessor "io.micronaut:micronaut-inject-java"
    implementation "io.micronaut:micronaut-inject-java"

    implementation("io.vavr:vavr:${vavrVersion}")

    testAnnotationProcessor "io.micronaut:micronaut-inject-java"

    testImplementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("io.micronaut.test:micronaut-test-core")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-inject")
    testImplementation("io.micronaut:micronaut-inject-java")

    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation('org.junit.jupiter:junit-jupiter')
    testImplementation("org.assertj:assertj-core:${assertjVersion}")
     */


    implementation(project(":annotations"))
    annotationProcessor(project(":annotation-processor"))
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
//
//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}