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
    testImplementation("junit:junit:4.13.1")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    api(project(":les-chats:data-access-layer"))
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.hsqldb:hsqldb:2.7.1")
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.5")
    api("org.springframework:spring-web:6.0.7")
    api("org.springframework.boot:spring-boot-devtools:3.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
