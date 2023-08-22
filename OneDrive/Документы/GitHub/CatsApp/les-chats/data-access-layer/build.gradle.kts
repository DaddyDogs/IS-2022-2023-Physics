plugins {
    id("java-library")
}

group = "ru.ermolaayyyyyyy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    compileOnly("org.hibernate.orm:hibernate-gradle-plugin:6.2.0.CR3")
    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.postgresql:postgresql:42.5.4")
    api("org.springframework.boot:spring-boot:3.0.5")
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.0.5")
    api("org.springframework.boot:spring-boot-starter-validation:3.0.5")
    api("org.springframework.boot:spring-boot-starter-security:3.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
