plugins {
    id("java-library")
}

group = "ru.ermolaayyyyyyy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //implementation(project(":les-chats:data-access-layer"))
    implementation("org.projectlombok:lombok:1.18.22")
    implementation("org.springframework.boot:spring-boot-starter-web:3.0.5")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
    implementation("net.kaczmarzyk:specification-arg-resolver:3.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.5")
    testImplementation("org.dbunit:dbunit:2.7.3")
    testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
    testImplementation("org.hsqldb:hsqldb:2.7.1")
    testImplementation("org.springframework.security:spring-security-test:6.0.3")
    implementation("org.springframework.security:spring-security-taglibs:6.0.3")
    api(project(":les-chats:service-layer"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
