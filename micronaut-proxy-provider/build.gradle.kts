//plugins {
//    id("java")
//}
plugins {
    id("io.richard.event.processor.java-library-conventions")
}

dependencies {
    implementation("org.slf4j:slf4j-api")
    implementation("org.slf4j:slf4j-simple")

    implementation(project(":annotations"))
    annotationProcessor(project(":annotation-processor"))
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}