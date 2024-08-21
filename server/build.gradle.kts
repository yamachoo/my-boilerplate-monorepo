import com.google.cloud.tools.jib.gradle.JibTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.graphql.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.jib)
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
    implementation(libs.graphql.kotlin.spring.server)
    implementation(libs.graphql.java.extended.scalars)
    implementation(libs.spring.boot.starter.validation)

    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.spring.boot.starter.r2dbc)
    implementation(libs.komapper.dialect.mysql.r2dbc)
    ksp(libs.komapper.processor)

    developmentOnly(libs.spring.boot.devtools)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.mockk)
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

jib {
    from {
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = project.name
    }
    container {
        user = "1100:1100"
    }
}

tasks.withType<JibTask> {
    notCompatibleWithConfigurationCache("because https://github.com/GoogleContainerTools/jib/issues/3132")
}
