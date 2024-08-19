plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.expediagroup.graphql") version "7.1.4"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "com.yamachoo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.expediagroup:graphql-kotlin-spring-server:7.1.4")
    implementation("com.graphql-java:graphql-java-extended-scalars:22.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    platform("org.komapper:komapper-platform:2.2.0").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-spring-boot-starter-r2dbc")
    implementation("org.komapper:komapper-dialect-mysql-r2dbc")
    ksp("org.komapper:komapper-processor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

detekt {
    source.setFrom(
        "settings.gradle.kts",
        "build.gradle.kts",
        "src/main/kotlin",
        "src/test/kotlin",
    )
    parallel = true
    autoCorrect = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
