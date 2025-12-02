plugins {
    java
    application
    id("org.springframework.boot") version "3.5.0"
}

group = "ru.itrum"
version = "0.0.1"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

springBoot {
    mainClass = "ru.itrum.api.ItrumApplication"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.0")
    implementation("org.liquibase:liquibase-core:4.28.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("net.lbruun.springboot:preliquibase-spring-boot-starter:1.6.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("org.projectlombok:lombok:1.18.36")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.0")
    runtimeOnly("com.h2database:h2:2.3.232")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
